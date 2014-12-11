package com.whollyframework.base.dao.support;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;


public class HibernateSQLUtils extends AbstractSQLUtils {

    @Override
    public Object getFilterCriterions(){
        return criterionList.toArray(new Criterion[criterionList.size()]);
    }
    
    public Criterion[] toHibernateCriterions(){
        Criterion[] criterions = new Criterion[criterionList.size()];
        for (int i = 0; i < criterionList.size(); i++) {
            criterions[i] = criterionList.get(i).buildHibernateCriterion();
        }
        return criterions;
    }
    
    public void setOrderBy(Criteria c){
        for (int i = 0; i < orderByClauses.length; i++) {
            c.addOrder(orderByClauses[i].toOrder());
        }
    }
}
