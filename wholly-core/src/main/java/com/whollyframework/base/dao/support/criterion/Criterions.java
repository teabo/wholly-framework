package com.whollyframework.base.dao.support.criterion;

import java.util.Collection;


public class Criterions {

    public static Criterion isNull(String fieldname) {
        return new SingleCriterion(fieldname, SingleCriterion.Op.NULL);
    }
    
    public static Criterion isNotNull(String fieldname) {
        return new SingleCriterion(fieldname, SingleCriterion.Op.NOTNULL);
    }
    
    public static Criterion isEmpty(String fieldname) {
        return new SingleCriterion(fieldname, SingleCriterion.Op.EMPTY);
    }

    public static Criterion isNotEmpty(String fieldname) {
        return new SingleCriterion(fieldname, SingleCriterion.Op.NOTEMPTY);
    }

    public static Criterion eq(String fieldname, Object value) {
        return new SimpleCriterion(fieldname, value, SimpleCriterion.Op.EQ);
    }
    
    public static Criterion ne(String fieldname, Object value) {
        return new SimpleCriterion(fieldname, value, SimpleCriterion.Op.NE);
    }

    public static Criterion gt(String fieldname, Object value) {
        return new SimpleCriterion(fieldname, value, SimpleCriterion.Op.GT);
    }

    public static Criterion ge(String fieldname, Object value) {
        return new SimpleCriterion(fieldname, value, SimpleCriterion.Op.GE);
    }

    public static Criterion lt(String fieldname, Object value) {
        return new SimpleCriterion(fieldname, value, SimpleCriterion.Op.LT);
    }
    
    public static Criterion le(String fieldname, Object value) {
        return new SimpleCriterion(fieldname, value, SimpleCriterion.Op.LE);
    }

    public static Criterion like(String fieldname, Object value) {
        return new SimpleCriterion(fieldname, value, SimpleCriterion.Op.LIKE);
    }
    
    public static Junction disjunction() {
        return new Junction(Junction.Op.OR);
    }
    
    public static Junction conjunction() {
        return new Junction(Junction.Op.AND);
    }
    
    public static Criterion in(String fieldname, Collection<Object> values){
        return new MultiValuedCriterion(fieldname, values.toArray(), MultiValuedCriterion.Op.IN);
    }
    
    public static Criterion in(String fieldname, Object[] values){
        return new MultiValuedCriterion(fieldname, values, MultiValuedCriterion.Op.IN);
    }
    
    public static Criterion notin(String fieldname, Collection<Object> values){
        return new MultiValuedCriterion(fieldname, values.toArray(), MultiValuedCriterion.Op.NOT_IN);
    }
    
    public static Criterion notin(String fieldname, Object[] values){
        return new MultiValuedCriterion(fieldname, values, MultiValuedCriterion.Op.NOT_IN);
    }
    
    public static Criterion between(String fieldname, Object value1, Object value2){
        return new MultiValuedCriterion(fieldname, new Object[]{value1, value2}, MultiValuedCriterion.Op.BETWEEN);
    }
    
    public static Criterion sqlRestriction(String sql) {
        return new SQLCriterion(sql);
    }
    
}
