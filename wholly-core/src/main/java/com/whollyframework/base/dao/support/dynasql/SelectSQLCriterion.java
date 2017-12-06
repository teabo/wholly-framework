package com.whollyframework.base.dao.support.dynasql;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class SelectSQLCriterion extends AbstractSQLCriterion {

    public SelectSQLCriterion () {
        super(Op.SELECT);
    }

    public SelectSQLCriterion setSearch(String sql) {
        criteria.put("sql", sql);
        return this;
    }

    public SelectSQLCriterion setSearch(String table_name, String[] fieldNames) {
        return setSearch(table_name, Arrays.asList(fieldNames));
    }

    public SelectSQLCriterion setSearch(String table_name, List<String> fieldNames) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ");
        for (Iterator<String> iterator = fieldNames.iterator(); iterator.hasNext();) {
            String string = (String) iterator.next();
            sql.append(string);
            if (iterator.hasNext()) {
                sql.append(", ");
            }
        }
        sql.append(" FROM ").append(table_name).append(" ");

        return setSearch(sql.toString());
    }
}
