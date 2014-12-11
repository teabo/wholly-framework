package com.whollyframework.base.dao.support.dynasql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class UpdateSQLCriterion extends AbstractSQLCriterion {

    public UpdateSQLCriterion () {
        super(Op.UPDATE);
    }
    
    public UpdateSQLCriterion setUpdate(String table_name, Map<String, Object> fields) {
        criteria.put("tableName", table_name);
        List<Entry<String, Object>> list = new ArrayList<Map.Entry<String, Object>>();
        list.addAll(fields.entrySet());
        criteria.put("fields", list);
        return this;
    }

    public UpdateSQLCriterion setUpdate(String table_name, String[] fieldNames, Object[] fieldValues) {
        return setUpdate(table_name, Arrays.asList(fieldNames), Arrays.asList(fieldValues));
    }

    public UpdateSQLCriterion setUpdate(String table_name, List<String> fieldNames, List<Object> fieldValues) {
        Map<String, Object> fields = new HashMap<String, Object>();
        for (int i = 0; i < fieldNames.size(); i++) {
            fields.put(fieldNames.get(i), fieldValues.get(i));
        }
        return setUpdate(table_name, fields);
    }
}
