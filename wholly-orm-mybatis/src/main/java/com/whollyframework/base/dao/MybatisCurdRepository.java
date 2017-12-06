package com.whollyframework.base.dao;

import java.io.Serializable;

/**
 * 需要在mapper xml中配置与方法名对应执行sql statment 这里定义接口只为统一与curd操作类的方法，实际应用可灵活处理
 * 
 * @author Chris Hsu
 * 
 * @param <E>
 * @param <ID>
 */
public interface MybatisCurdRepository<E, ID extends Serializable> extends IDesignDAO<E, ID>{
	
}
