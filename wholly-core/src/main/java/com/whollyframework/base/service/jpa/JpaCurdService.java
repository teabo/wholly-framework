package com.whollyframework.base.service.jpa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
/**
 * jpa的crud实现需要注入PagingAndSortingRepository
 * @author zhangzhongmin
 *
 * @param <T>
 * @param <ID>
 */
public abstract class JpaCurdService<T, ID extends Serializable> {

	public abstract PagingAndSortingRepository<T,ID> getRepository();

	public List<T> getAll() {
		List<T> list = new ArrayList<T>();
		Iterator<T> it = getRepository().findAll().iterator();
		while (it.hasNext()) {
			list.add(it.next());
		}
		return list;
	}

	public T find(ID id) throws Exception {
		return getRepository().findOne(id);
	}

	public void doCreate(T vo) throws Exception {
		getRepository().save(vo);
	}

	public void doUpdate(T vo) throws Exception {
		getRepository().save(vo);
	}

	public void doRemove(T vo) throws Exception {
		getRepository().delete(vo);
	}

	public void doRemove(ID[] selects) throws Exception {
		for (int i = 0; i < selects.length; i++) {
			getRepository().delete(selects[i]);
		}
	}

	public void doRemove(ID pk) throws Exception {
		getRepository().delete(pk);
	}

	public boolean isEmpty() throws Exception {
		return getRepository().count()<=0;
	}

}
