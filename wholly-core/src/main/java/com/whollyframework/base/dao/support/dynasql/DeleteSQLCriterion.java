package com.whollyframework.base.dao.support.dynasql;

public class DeleteSQLCriterion extends AbstractSQLCriterion {

    public DeleteSQLCriterion () {
        super(Op.DELETE);
    }
    
    public DeleteSQLCriterion setDelete(String table_name) {
        criteria.put("tableName", table_name);
        return this;
    }
}
