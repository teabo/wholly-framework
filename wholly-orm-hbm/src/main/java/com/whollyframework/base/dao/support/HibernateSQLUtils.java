package com.whollyframework.base.dao.support;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;

import com.whollyframework.base.dao.support.OrderByClause.SortOrder;
import com.whollyframework.base.dao.support.criterion.HibernateCriterions;

public class HibernateSQLUtils extends AbstractSQLUtils {

	@Override
	public Object getFilterCriterions() {
		return criterionList.toArray(new Criterion[criterionList.size()]);
	}

	public Criterion[] toHibernateCriterions() {
		Criterion[] criterions = new Criterion[criterionList.size()];
		for (int i = 0; i < criterionList.size(); i++) {
			criterions[i] = HibernateCriterions.transform(criterionList.get(i));
		}
		return criterions;
	}

	public void setOrderBy(Criteria c) {
		for (int i = 0; i < orderByClauses.length; i++) {
			c.addOrder(toOrder(orderByClauses[i]));
		}
	}

	public Order toOrder(OrderByClause order) {
		return (order.sortOrder == SortOrder.DESC) ? Order.desc(order.columnName) : Order.asc(order.columnName);
	}
}
