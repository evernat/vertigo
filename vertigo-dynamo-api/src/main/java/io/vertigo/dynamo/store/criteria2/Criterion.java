package io.vertigo.dynamo.store.criteria2;

import java.util.function.Predicate;

import io.vertigo.dynamo.domain.metamodel.DtDefinition;
import io.vertigo.dynamo.domain.metamodel.DtFieldName;
import io.vertigo.dynamo.domain.model.Entity;
import io.vertigo.dynamo.domain.util.DtObjectUtil;
import io.vertigo.lang.Assertion;

public final class Criterion<E extends Entity> implements CriteriaBool<E> {
	private final DtFieldName dtFieldName;
	private final CriterionOperator criterionOperator;
	private final Comparable value1;
	private final Comparable value2;

	Criterion(final DtFieldName dtFieldName, final CriterionOperator criterionOperator) {
		Assertion.checkNotNull(dtFieldName);
		Assertion.checkNotNull(criterionOperator);
		Assertion.checkArgument(criterionOperator.getArity() == 0, "Only zero argument functions are allowed");
		//---
		this.criterionOperator = criterionOperator;
		this.dtFieldName = dtFieldName;
		value1 = null;
		value2 = null;
	}

	Criterion(final DtFieldName dtFieldName, final CriterionOperator criterionOperator, final Comparable value) {
		Assertion.checkNotNull(dtFieldName);
		Assertion.checkNotNull(criterionOperator);
		Assertion.checkArgument(criterionOperator.getArity() == 1, "Only one argument functions are allowed");
		Assertion.checkNotNull(value);
		//---
		this.criterionOperator = criterionOperator;
		this.dtFieldName = dtFieldName;
		value1 = value;
		value2 = null;
	}

	Criterion(final DtFieldName dtFieldName, final CriterionOperator criterionOperator, final Comparable value1, final Comparable value2) {
		Assertion.checkNotNull(dtFieldName);
		Assertion.checkNotNull(criterionOperator);
		Assertion.checkArgument(criterionOperator.getArity() == 2, "Only two arguments functions are allowed");
		Assertion.checkNotNull(value1);
		Assertion.checkNotNull(value2);
		//---
		this.criterionOperator = criterionOperator;
		this.dtFieldName = dtFieldName;
		this.value1 = value1;
		this.value2 = value2;
	}

	public CriterionOperator getOperator() {
		return criterionOperator;
	}

	public DtFieldName getDtFieldName() {
		return dtFieldName;
	}

	public Object getValue1() {
		return value1;
	}

	public Object getValue2() {
		return value2;
	}

	@Override
	public String toSql(final Ctx ctx) {
		switch (criterionOperator) {
			case IS_NOT_NULL:
				return dtFieldName + " is not null";
			case IS_NULL:
				return dtFieldName + " is null";
			case EQ:
				return dtFieldName + " = #" + ctx.attributeName(dtFieldName, value1) + "#";
			case NEQ:
				return dtFieldName + " != #" + ctx.attributeName(dtFieldName, value1) + "#";
			case GT:
				return dtFieldName + " > #" + ctx.attributeName(dtFieldName, value1) + "#";
			case GTE:
				return dtFieldName + " >= #" + ctx.attributeName(dtFieldName, value1) + "#";
			case LT:
				return dtFieldName + " < #" + ctx.attributeName(dtFieldName, value1) + "#";
			case LTE:
				return dtFieldName + " <= #" + ctx.attributeName(dtFieldName, value1) + "#";
			case BETWEEN:
				return "(" + dtFieldName + " >= #" + ctx.attributeName(dtFieldName, value1) + "# and " + dtFieldName + " <= #" + ctx.attributeName(dtFieldName, value2) + "# )";
			case STARTS_WITH:
				return dtFieldName + " like  #" + ctx.attributeName(dtFieldName, value1) + "# || " + "'%%'";
			default:
				throw new IllegalAccessError();
		}
	}

	@Override
	public Predicate<E> toPredicate() {
		return entity -> test(entity);
	}

	private boolean test(final E entity) {
		final DtDefinition entitytDefinition = DtObjectUtil.findDtDefinition(entity.getClass());
		final Object value = entitytDefinition.getField(dtFieldName).getDataAccessor().getValue(entity);

		switch (criterionOperator) {
			case IS_NOT_NULL:
				return value != null;
			case IS_NULL:
				return value == null;
			case EQ:
				return value1.equals(value);
			case NEQ:
				return !value1.equals(value);
			case GT:
				return value1.compareTo(value) < 0;
			case GTE:
				return value1.compareTo(value) <= 0;
			case LT:
				return value1.compareTo(value) > 0;
			case LTE:
				return value1.compareTo(value) >= 0;
			case BETWEEN:
				return value1.compareTo(value) <= 0 && value2.compareTo(value) >= 0;
			case STARTS_WITH:
				return String.class.cast(value).startsWith((String) value1);
			default:
				throw new IllegalAccessError();
		}
	}
}