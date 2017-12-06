package com.whollyframework.base.dao.support.criterion;

import java.util.Collection;


public class Criterions {

    public static Criterion isNull(String fieldname) {
        return new SingleCriterion(fieldname, SingleCriterion.Operation.NULL);
    }
    
    public static Criterion isNotNull(String fieldname) {
        return new SingleCriterion(fieldname, SingleCriterion.Operation.NOTNULL);
    }
    
    public static Criterion isEmpty(String fieldname) {
        return new SingleCriterion(fieldname, SingleCriterion.Operation.EMPTY);
    }

    public static Criterion isNotEmpty(String fieldname) {
        return new SingleCriterion(fieldname, SingleCriterion.Operation.NOTEMPTY);
    }

    public static Criterion eq(String fieldname, Object value) {
        return new SimpleCriterion(fieldname, value, SimpleCriterion.Operation.EQ);
    }
    
    public static Criterion ne(String fieldname, Object value) {
        return new SimpleCriterion(fieldname, value, SimpleCriterion.Operation.NE);
    }

    public static Criterion gt(String fieldname, Object value) {
        return new SimpleCriterion(fieldname, value, SimpleCriterion.Operation.GT);
    }

    public static Criterion ge(String fieldname, Object value) {
        return new SimpleCriterion(fieldname, value, SimpleCriterion.Operation.GE);
    }

    public static Criterion lt(String fieldname, Object value) {
        return new SimpleCriterion(fieldname, value, SimpleCriterion.Operation.LT);
    }
    
    public static Criterion le(String fieldname, Object value) {
        return new SimpleCriterion(fieldname, value, SimpleCriterion.Operation.LE);
    }

    public static Criterion like(String fieldname, Object value) {
        return new SimpleCriterion(fieldname, value, SimpleCriterion.Operation.LIKE);
    }
    
    public static Junction disjunction() {
        return new Junction(Junction.Operation.OR);
    }
    
    public static Junction conjunction() {
        return new Junction(Junction.Operation.AND);
    }
    
    public static Criterion in(String fieldname, Collection<Object> values){
        return new MultiValuedCriterion(fieldname, values.toArray(), MultiValuedCriterion.Operation.IN);
    }
    
    public static Criterion in(String fieldname, Object[] values){
        return new MultiValuedCriterion(fieldname, values, MultiValuedCriterion.Operation.IN);
    }
    
    public static Criterion notin(String fieldname, Collection<Object> values){
        return new MultiValuedCriterion(fieldname, values.toArray(), MultiValuedCriterion.Operation.NOT_IN);
    }
    
    public static Criterion notin(String fieldname, Object[] values){
        return new MultiValuedCriterion(fieldname, values, MultiValuedCriterion.Operation.NOT_IN);
    }
    
    public static Criterion between(String fieldname, Object value1, Object value2){
        return new MultiValuedCriterion(fieldname, new Object[]{value1, value2}, MultiValuedCriterion.Operation.BETWEEN);
    }
    
    public static Criterion sqlRestriction(String sql) {
        return new SQLCriterion(sql);
    }
    
}
