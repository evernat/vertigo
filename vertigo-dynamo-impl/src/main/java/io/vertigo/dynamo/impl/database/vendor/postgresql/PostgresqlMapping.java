package io.vertigo.dynamo.impl.database.vendor.postgresql;

import io.vertigo.dynamo.database.vendor.SQLMapping;
import io.vertigo.dynamo.domain.metamodel.DataType;
import io.vertigo.dynamo.impl.database.vendor.core.SQLMappingImpl;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Implémentation spécifique à Postgresql.
 *
 * @author pchretien
 */
final class PostgresqlMapping implements SQLMapping {
	private final SQLMapping defaultSQLMapping = new SQLMappingImpl();

	/** {@inheritDoc} */
	public int getTypeSQL(final DataType dataType) {
		if (dataType == DataType.Boolean) {
			return Types.BOOLEAN;
		}
		return defaultSQLMapping.getTypeSQL(dataType);
	}

	/** {@inheritDoc} */
	public void setValueOnStatement(final java.sql.PreparedStatement statement, final int index, final DataType dataType, final Object value) throws SQLException {
		if (value == null) {
			defaultSQLMapping.setValueOnStatement(statement, index, dataType, null /*value*/);
		} else if (dataType == DataType.Boolean) {
			statement.setBoolean(index, Boolean.TRUE.equals(value));
		} else {
			defaultSQLMapping.setValueOnStatement(statement, index, dataType, value);
		}
	}

	/** {@inheritDoc} */
	public Object getValueForCallableStatement(final CallableStatement callableStatement, final int index, final DataType dataType) throws SQLException {
		if (dataType == DataType.Boolean) {
			final boolean vb = callableStatement.getBoolean(index);
			return callableStatement.wasNull() ? null : vb;
		}
		return defaultSQLMapping.getValueForCallableStatement(callableStatement, index, dataType);
	}

	/** {@inheritDoc} */
	public Object getValueForResultSet(final ResultSet rs, final int col, final DataType dataType) throws SQLException {
		if (dataType == DataType.Boolean) {
			final boolean vb = rs.getBoolean(col);
			return rs.wasNull() ? null : vb;
		}
		return defaultSQLMapping.getValueForResultSet(rs, col, dataType);
	}

	/** {@inheritDoc} */
	public DataType getDataType(final int typeSQL) {
		return defaultSQLMapping.getDataType(typeSQL);
	}
}
