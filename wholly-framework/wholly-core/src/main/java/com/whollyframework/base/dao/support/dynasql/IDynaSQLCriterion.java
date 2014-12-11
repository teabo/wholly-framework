package com.whollyframework.base.dao.support.dynasql;

import java.util.Map;

public interface IDynaSQLCriterion {
    
    boolean isValid();
    
    String getOp() ;
    
    Map<String, Object> getCriteria();
}
