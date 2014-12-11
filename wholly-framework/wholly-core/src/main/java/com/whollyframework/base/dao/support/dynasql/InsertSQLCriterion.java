package com.whollyframework.base.dao.support.dynasql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class InsertSQLCriterion extends AbstractSQLCriterion {

    public InsertSQLCriterion () {
        super(Op.INSERT);
    }
    
    public InsertSQLCriterion setInsert(String table_name, Map<String, Object> fields) {
        List<String> fieldNames = new ArrayList<String>();
        List<Object> fieldValues = new ArrayList<Object>();
        for (Iterator<Map.Entry<String, Object>> it = fields.entrySet().iterator(); it.hasNext();) {
            Map.Entry<String, Object> entry = it.next();
            fieldNames.add(entry.getKey());
            fieldValues.add(entry.getValue());
        }
        return setInsert(table_name, fieldNames, fieldValues);
    }

    public InsertSQLCriterion setInsert(String table_name, String[] fieldNames, Object[] fieldValues) {
        return setInsert(table_name, Arrays.asList(fieldNames), Arrays.asList(fieldValues));
    }

    public InsertSQLCriterion setInsert(String table_name, List<String> fieldNames, List<Object> fieldValues) {
        criteria.put("tableName", table_name);
        criteria.put("fieldNames", fieldNames);
        criteria.put("fieldValues", fieldValues);
        return this;
    }
}
