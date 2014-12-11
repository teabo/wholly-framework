package com.whollyframework.base.dao.support.criterion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.hibernate.criterion.Restrictions;

public class Junction implements Criterion {
    private final List<Criterion> conditions = new ArrayList<Criterion>();
    private final Op op;

    /**
     */
    public enum Op {
        OR, AND;
        
        public String getValueType(){
            switch (this) {
                case OR:
                    return " or ";
                case AND:
                    return " and ";
                default:
                    return null;
            }
        }
    }
    
    public Junction(Op op, Criterion... criterion){
        this.op = op;
        Collections.addAll( conditions, criterion );
    }
    
    /**
     * Adds a criterion to the junction (and/or)
     *
     * @param criterion The criterion to add
     *
     * @return {@code this}, for method chaining
     */
    public Junction add(Criterion criterion) {
        conditions.add( criterion );
        return this;
    }

    public List<Criterion> getConditions() {
        return conditions;
    }
    
    public String getOp() {
        return op.getValueType();
    }
    
    public String toSqlString() {
        if ( conditions.size()==0 ) {
            return "1=1";
        }

        final StringBuilder buffer = new StringBuilder().append( '(' );
        final Iterator<Criterion> itr = conditions.iterator();
        while ( itr.hasNext() ) {
            buffer.append( ( itr.next() ).toSqlString() );
            if ( itr.hasNext() ) {
                buffer.append( getOp() );
            }
        }

        return buffer.append( ')' ).toString();
    }
    
    public Object[] getParamValues() {
        final ArrayList<Object> typedValues = new ArrayList<Object>();
        for ( Criterion condition : conditions ) {
            final Object[] subValues = condition.getParamValues();
            Collections.addAll( typedValues, subValues );
        }
        return typedValues.toArray( new Object[ typedValues.size() ] );
    }
    
    public org.hibernate.criterion.Criterion buildHibernateCriterion(){
        org.hibernate.criterion.Junction junction;
        switch (op) {
            case OR:
                junction = Restrictions.disjunction();
                break;
            case AND:
                junction = Restrictions.conjunction();
                break;
            default:
                return null;
        }
        for ( Criterion condition : conditions ) {
            junction.add(condition.buildHibernateCriterion());
        }
        return junction;
    }

    public String getType(){
        return "Junction";
    }
}
