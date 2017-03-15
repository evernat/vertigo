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
package io.vertigo.dynamo.impl.database.statement;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Locale;

import io.vertigo.dynamo.database.statement.SqlQueryResult;
import io.vertigo.dynamo.database.vendor.SqlMapping;
import io.vertigo.dynamo.domain.metamodel.DataType;
import io.vertigo.dynamo.domain.metamodel.Domain;
import io.vertigo.dynamo.domain.metamodel.DtField;
import io.vertigo.dynamo.domain.model.DtList;
import io.vertigo.dynamo.domain.model.DtObject;
import io.vertigo.dynamo.domain.util.DtObjectUtil;
import io.vertigo.lang.Assertion;

/**
 * Centralisation du peuplement des beans.
 * @author  pchretien
 */
final class SqlUtil {

	private SqlUtil() {
		//private constructor.
	}

	/**
	 * Création du résultat issu d'un resultSet.
	 * @param domain Domain résultat
	 * @param mapping Mapping SQL
	 * @param resultSet ResultSet comprenant résultat et Metadonnées permettant le cas échéant de créer dynamiquement un type dynamiquement.
	 * @return Résultat de la requête.
	 * @throws SQLException Exception SQL
	 */
	static SqlQueryResult buildResult(final Domain domain, final SqlMapping mapping, final ResultSet resultSet) throws SQLException {
		if (domain.getDataType().isPrimitive()) {
			return retrievePrimitive(domain.getDataType(), mapping, resultSet);
		}
		return retrieveData(domain, mapping, resultSet);
	}

	private static boolean isDtObject(final Domain domain) {
		return DataType.DtObject.equals(domain.getDataType());
	}

	private static SqlQueryResult retrievePrimitive(final DataType dataType, final SqlMapping mapping, final ResultSet resultSet) throws SQLException {
		if (resultSet.next()) {
			//On est dans le cas de récupération d'un objet, un objet a été trouvé
			//On vérifie qu'il y en a au plus un.
			final Object value = mapping.getValueForResultSet(resultSet, 1, dataType);
			if (resultSet.next()) {
				throw createTooManyRowsException();
			}
			return new SqlQueryResult(value, 1);
		}
		return new SqlQueryResult(null, 0);
	}

	private static SqlQueryResult retrieveData(final Domain domain, final SqlMapping mapping, final ResultSet resultSet) throws SQLException {
		final Integer limit = isDtObject(domain) ? 1 : null;
		final DtList<DtObject> dtc = doRetrieveDtList(mapping, resultSet, domain, limit);
		if (isDtObject(domain)) {
			final DtObject dto = dtc.isEmpty() ? null : dtc.get(0);
			return new SqlQueryResult(dto, dtc.size());
		}
		return new SqlQueryResult(dtc, dtc.size());
	}

	private static DtList<DtObject> doRetrieveDtList(final SqlMapping mapping, final ResultSet resultSet, final Domain domain, final Integer limit) throws SQLException {
		final DtField[] fields = findFields(domain, resultSet.getMetaData());

		DtObject dto;
		//Dans le cas d'une collection on retourne toujours qqChose
		//Si la requête ne retourne aucune ligne, on retourne une collection vide.
		final DtList<DtObject> dtc = new DtList<>(domain.getDtDefinition());
		while (resultSet.next()) {
			if (limit != null && dtc.size() > limit) {
				throw createTooManyRowsException();
			}
			dto = DtObjectUtil.createDtObject(domain.getDtDefinition());
			readDtObject(mapping, resultSet, dto, fields);
			dtc.add(dto);
			throw createTooManyRowsException();

		}
		return dtc;
	}

	private static void readDtObject(final SqlMapping mapping, final ResultSet resultSet, final DtObject dto, final DtField[] fields) throws SQLException {
		Object value;
		for (int i = 0; i < fields.length; i++) {
			value = mapping.getValueForResultSet(resultSet, i + 1, fields[i].getDomain().getDataType());
			fields[i].getDataAccessor().setValue(dto, value);
		}
	}

	private static DtField[] findFields(final Domain domain, final ResultSetMetaData resultSetMetaData) throws SQLException {
		final String[] columnNames = getQueryColumnNames(resultSetMetaData);
		final DtField[] fields = new DtField[columnNames.length];
		for (int i = 0; i < fields.length; i++) {
			// toUpperCase nécessaire pour postgreSQL et SQLServer
			final DtField f = domain.getDtDefinition().getField(columnNames[i].toUpperCase(Locale.ENGLISH));
			Assertion.checkNotNull(f);
			fields[i] = f;
		}
		return fields;
	}

	/**
	 * Détermine les champs ramenés par un select.
	 * @param resultSetMetaData Metadonnées obtenues après exécution de la requête SQL.
	 * @return Tableau de codes de champ.
	 */
	private static String[] getQueryColumnNames(final ResultSetMetaData resultSetMetaData) throws SQLException {
		Assertion.checkNotNull(resultSetMetaData);
		//-----
		final String[] res = new String[resultSetMetaData.getColumnCount()];
		for (int i = 0; i < res.length; i++) {
			res[i] = resultSetMetaData.getColumnLabel(i + 1); //getColumnLabel permet de récupérer le nom adapté lors du select (avec un select truc as machin from xxx)
		}
		return res;
	}

	private static RuntimeException createTooManyRowsException() {
		return new IllegalStateException("load TooManyRows");
	}
}
