package com.whollyframework.base.dao.support.criterion;

import org.hibernate.criterion.Restrictions;

public class HibernateCriterions {

	public static org.hibernate.criterion.Criterion transform(Junction criterion) {
		org.hibernate.criterion.Junction junction;
		switch (criterion.getOperation()) {
		case OR:
			junction = Restrictions.disjunction();
			break;
		case AND:
			junction = Restrictions.conjunction();
			break;
		default:
			return null;
		}
		for (Criterion condition : criterion.getConditions()) {
			junction.add(HibernateCriterions.transform(condition));
		}
		return junction;
	}

	public static org.hibernate.criterion.Criterion transform(Criterion criterion) {
		if (criterion instanceof Junction) {
			return transform((Junction) criterion);
		} else if (criterion instanceof MultiValuedCriterion) {
			return transform((MultiValuedCriterion) criterion);
		} else if (criterion instanceof NotCriterion) {
			return transform((NotCriterion) criterion);
		} else if (criterion instanceof SimpleCriterion) {
			return transform((SimpleCriterion) criterion);
		} else if (criterion instanceof SingleCriterion) {
			return transform((SingleCriterion) criterion);
		} else if (criterion instanceof SQLCriterion) {
			return transform((SQLCriterion) criterion);
		}
		return null;
	}

	public static org.hibernate.criterion.Criterion transform(MultiValuedCriterion criterion) {
		switch (criterion.getOperation()) {
		case IN:
			return Restrictions.in(criterion.getPropertyName(), criterion.getParamValues());
		case NOT_IN:
			return Restrictions.not(Restrictions.in(criterion.getPropertyName(), criterion.getParamValues()));
		case BETWEEN:
			return Restrictions.between(criterion.getPropertyName(), criterion.getParamValues()[0],
					criterion.getParamValues()[1]);
		default:
			return null;
		}
	}

	public static org.hibernate.criterion.Criterion transform(NotCriterion criterion) {
		return Restrictions.not(HibernateCriterions.transform(criterion.getCriterion()));
	}

	public static org.hibernate.criterion.Criterion transform(SimpleCriterion criterion) {
		switch (criterion.getOperation()) {
		case EQ:
			return Restrictions.eq(criterion.getPropertyName(), criterion.getValue());
		case NE:
			return Restrictions.ne(criterion.getPropertyName(), criterion.getValue());
		case LE:
			return Restrictions.le(criterion.getPropertyName(), criterion.getValue());
		case GE:
			return Restrictions.ge(criterion.getPropertyName(), criterion.getValue());
		case LT:
			return Restrictions.lt(criterion.getPropertyName(), criterion.getValue());
		case GT:
			return Restrictions.gt(criterion.getPropertyName(), criterion.getValue());
		case LIKE:
			return Restrictions.like(criterion.getPropertyName(), criterion.getValue());
		default:
			return null;
		}
	}

	public static org.hibernate.criterion.Criterion transform(SingleCriterion criterion) {
		switch (criterion.getOperation()) {
		case NULL:
			return Restrictions.isNull(criterion.getPropertyName());
		case NOTNULL:
			return Restrictions.isNotNull(criterion.getPropertyName());
		case EMPTY:
			return Restrictions.isEmpty(criterion.getPropertyName());
		case NOTEMPTY:
			return Restrictions.isNotEmpty(criterion.getPropertyName());
		default:
			return null;
		}
	}

	public static org.hibernate.criterion.Criterion transform(SQLCriterion criterion) {
		return Restrictions.sqlRestriction(criterion.toSqlString());
	}

}
