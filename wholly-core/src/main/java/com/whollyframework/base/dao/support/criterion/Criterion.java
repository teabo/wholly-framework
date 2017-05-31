package com.whollyframework.base.dao.support.criterion;

public interface Criterion {
    
    String toSqlString();
    
    Object[] getParamValues();
}
