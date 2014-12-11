package com.whollyframework.base.dao.support.dynasql;

import java.util.Map;

public class AbstractSQLCriterion implements IDynaSQLCriterion {
    
    protected Map<String, Object> criteria;
    private final Op              op;
    
    public AbstractSQLCriterion (Op op) {
        this.op = op;
    }
    
    /**
     */
    public enum Op {
        INSERT, UPDATE, DELETE, SELECT;
        
        public String getValueType() {
            switch (this) {
                case INSERT:
                    return "INSERT";
                case UPDATE:
                    return "UPDATE";
                case DELETE:
                    return "DELETE";
                case SELECT:
                    return "SELECT";
                default:
                    return null;
            }
        }
    }
    
    public String getOp() {
        return op.getValueType();
    }
    
    public Map<String, Object> getCriteria() {
        return criteria;
    }
    
    public boolean isValid() {
        return criteria.size() > 0;
    }

}
