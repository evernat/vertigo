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
/**
 *
 */
package io.vertigo.dynamox.task;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.vertigo.commons.script.ScriptManager;
import io.vertigo.commons.transaction.VTransactionManager;
import io.vertigo.database.sql.SqlDataBaseManager;
import io.vertigo.database.sql.connection.SqlConnection;
import io.vertigo.database.sql.parser.SqlNamedParam;
import io.vertigo.database.sql.statement.SqlParameter;
import io.vertigo.database.sql.statement.SqlPreparedStatement;
import io.vertigo.dynamo.domain.metamodel.DataType;
import io.vertigo.dynamo.domain.model.DtList;
import io.vertigo.dynamo.store.StoreManager;
import io.vertigo.dynamo.task.metamodel.TaskAttribute;
import io.vertigo.lang.Assertion;

/**
 * @author jmforhan
 */
public final class TaskEngineProcBatch extends AbstractTaskEngineSQL {
	/**
	 * Constructeur.
	 * @param scriptManager Manager de traitment de scripts
	 */
	@Inject
	public TaskEngineProcBatch(
			final ScriptManager scriptManager,
			final VTransactionManager transactionManager,
			final StoreManager storeManager,
			final SqlDataBaseManager sqlDataBaseManager) {
		super(scriptManager, transactionManager, storeManager, sqlDataBaseManager);
	}

	/** {@inheritDoc} */
	@Override
	public int doExecute(
			final String sql,
			final SqlConnection connection,
			final SqlPreparedStatement statement,
			final List<SqlNamedParam> params) throws SQLException {
		Assertion.checkArgNotEmpty(sql);
		Assertion.checkNotNull(connection);
		Assertion.checkNotNull(statement);
		Assertion.checkNotNull(params);
		//---
		// on alimente le batch.
		// La taille du batch est déduite de la taille de la collection en entrée.
		final int batchSize = getBatchSize();
		final List<List<SqlParameter>> batch = new ArrayList<>();
		for (int rowNumber = 0; rowNumber < batchSize; rowNumber++) {
			final List<SqlParameter> sqlParameters = new ArrayList<>();
			for (final SqlNamedParam param : params) {
				sqlParameters.add(buildSqlParameter(param, rowNumber));
			}
			batch.add(sqlParameters);
		}

		return statement.executeBatch(sql, batch);
	}

	private int getBatchSize() {
		Integer batchSize = null;
		for (final TaskAttribute attribute : getTaskDefinition().getInAttributes()) {
			if (attribute.getDomain().getDataType() == DataType.DtList) {
				Assertion.checkState(batchSize == null, "Pour un traitement Batch, il ne doit y avoir qu'une seule liste en entrée.");
				final DtList<?> dtc = getValue(attribute.getName());
				batchSize = dtc.size();
			}
		}
		Assertion.checkNotNull(batchSize, "Pour un traitement Batch, il doit y avoir une (et une seule) liste en entrée.");
		return batchSize;
	}
}
