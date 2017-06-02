/**
 * vertigo - simple java starter
 *
 * Copyright (C) 2013-2017, KleeGroup, direction.technique@kleegroup.com (http://www.kleegroup.com)
 * KleeGroup, Centre d'affaire la Boursidiere - BP 159 - 92357 Le Plessis Robinson Cedex - France
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vertigo.database.impl.sql.vendor.sqlserver;

import java.sql.SQLException;

import io.vertigo.database.impl.sql.vendor.core.AbstractSqlExceptionHandler;
import io.vertigo.database.sql.statement.SqlPreparedStatement;

/**
 * Handler des exceptions SQL qui peuvent survenir dans une tache.
 * Cette implémentation est adaptée pour SQL Server.
 *
 * @author jmainaud, npiedeloup
 */
final class SqlServerExceptionHandler extends AbstractSqlExceptionHandler {

	private enum SQLServerVersion {
		PostSQLServer2008("«\u00A0", "\u00A0»."), // \u00A0 : no-break-space (ne part pas avec un trim)
		PostSQLServer2008b("\"", "\"."), // autre facon de SQL serveur pour lever les contraintes (au moins sur les FK)
		PreSQLServer2008("'", "'.");

		private final String startQuote;
		private final String endQuote;

		SQLServerVersion(final String startQuote, final String endQuote) {
			this.startQuote = startQuote;
			this.endQuote = endQuote;
		}

		public String getStartQuote() {
			return startQuote;
		}

		public String getEndQuote() {
			return endQuote;
		}
	}

	/**
	 * Constructor.
	 */
	SqlServerExceptionHandler() {
		super();
	}

	/** {@inheritDoc} */
	@Override
	public void handleSQLException(final SQLException sqle, final SqlPreparedStatement statement) {

		final int errorCode = sqle.getErrorCode();

		if (errorCode >= 20_000 && errorCode < 30_000) {
			// Erreur utilisateur
			handleUserSQLException(sqle);
		} else if (errorCode == 8152) {
			// Valeur trop grande pour ce champs (#8152)
			handleTooLargeValueSqlException(sqle);
		} else if (errorCode == 547) {
			// Violation de contrainte d'intégrité référentielle (#547)
			handleForeignConstraintSQLException(sqle);
		} else if (errorCode == 2601 || errorCode == 2627) {
			// Violation de contrainte d'unicité (#2627)
			// Violation de contrainte d'unicité sur index (#2601) (attention message différent)
			handleUniqueConstraintSQLException(sqle);
		} else {
			// Message d'erreur par défaut
			handleOtherSQLException(sqle, statement);
		}
	}

	/** {@inheritDoc} */
	@Override
	protected String extractConstraintName(final String messageErreur) {
		String constraintName = null;
		for (final SQLServerVersion parsingMode : SQLServerVersion.values()) {
			constraintName = extractConstraintName(parsingMode, messageErreur);
			if (constraintName != null) {
				return constraintName;
			}
		}
		return constraintName;
	}

	private static String extractConstraintName(final SQLServerVersion version, final String messageErreur) {
		final int indexFin = messageErreur.indexOf(version.getEndQuote());
		if (indexFin != -1) {
			final int indexDebut = messageErreur.lastIndexOf(version.getStartQuote(), indexFin - 1);
			if (indexDebut != -1) {
				return messageErreur.substring(indexDebut + version.getStartQuote().length(), indexFin);
			}
		}
		return null;
	}
}