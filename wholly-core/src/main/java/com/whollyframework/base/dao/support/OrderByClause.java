package com.whollyframework.base.dao.support;

/**
 * Class that describes a sort order.
 * 
 * @author Chris Hsu
 * @since 2014-10-16
 */
public class OrderByClause {
    
    /**
     */
    public enum SortOrder {
        ASC, DESC;
        
        public String getSort() {
            if (this == DESC) {
                return " DESC ";
            }
            else if (this == ASC) {
                return " ASC ";
            }
            return "";
        }
    }
    
    public final String    columnName;
    
    public final SortOrder sortOrder;
    
    public OrderByClause (final SortOrder sortOrder, final String columnName) {
        this.sortOrder = sortOrder;
        this.columnName = columnName;
    }
    
    public String toString() {
        StringBuilder retVal = new StringBuilder().append(columnName).append(
                (sortOrder == SortOrder.DESC) ? " DESC " : " ASC ");
        
        return retVal.toString();
    }
    
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        
        OrderByClause that = (OrderByClause) o;
        
        if (!columnName.equals(that.columnName))
            return false;
        if (sortOrder != that.sortOrder)
            return false;
        
        return true;
    }
    
    public int hashCode() {
        int result = columnName.hashCode();
        result = 31 * result + sortOrder.hashCode();
        return result;
    }
}
