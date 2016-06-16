package com.xdragon.jpa.service;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import com.xdragon.jpa.persistence.DynamicSpecifications;
import com.xdragon.jpa.repository.BaseRepository;
import com.xdragon.utils.Sorts;

public abstract class BaseService<T, ID extends Serializable> {
	
	public T save(Map<String, Object> map) {
		return this.getRepository().save(map);
	}

	public T save(T entity) {
		return this.getRepository().save(entity);
	}

	public List<T> save(T... entities) {
		return this.getRepository().save(entities);
	}

	public Iterable<T> save(Iterable<T> entities) {
		return this.getRepository().save(entities);
	}

	public List<T> save(Iterator<T> entities) {
		return this.getRepository().save(entities);
	}

	public List<T> batchsave(Iterator<T> iterator, int count) {
		return this.getRepository().batchsave(iterator, count);
	}

	public List<T> batchsave(Iterable<T> iterable, int count) {
		return this.getRepository().batchsave(iterable, count);
	}

	public T update(String name, Object value, ID id) {
		merge(name, value, id);
		return findOne(id);
	}

	public T update(Map<String, Object> parameters, ID id) {
		return update(findOne(id), parameters);
	}

	public T update(Map<String, Object> parameters, Specification<T> spec) {
		return update(findOne(spec), parameters);
	}

	public T update(T t, Map<String, Object> parameters) {
		if (t == null) {
			return null;
		}
		try {
			BeanUtils.populate(t, parameters);
			return this.getRepository().merge(t);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
			return null;
		}
	}

	public int merge(Map<String, Object> parameters, ID id) {
		return this.getRepository().merge(parameters, id);
	}

	public int merge(Map<String, Object> parameters, Collection<ID> ids) {
		return this.getRepository().merge(parameters, ids.iterator());
	}

	public int merge(String key, Object value, ID id) {
		return this.getRepository().merge(key, value, id);
	}

	public int merge(String key, Object value, Collection<ID> ids) {
		return this.getRepository().merge(key, value, ids.iterator());
	}

	public int merge(Map<String, Object> map, Map<String, Object> conditions) {
		Specification<T> spec = DynamicSpecifications.buildSpecification(
				conditions, this.getRepository().getType());
		return this.getRepository().merge(map, spec);
	}

	public void delete(ID id) {
		this.getRepository().delete(id);
	}

	public void delete(T entity) {
		this.getRepository().delete(entity);
	}

	public void delete(T... entities) {
		this.getRepository().delete(entities);
	}

	public void delete(Iterable<T> entities) {
		this.getRepository().delete(entities);
	}

	public void delete(Iterator<T> entities) {
		this.getRepository().delete(entities);
	}

	public void deleteAll() {
		this.getRepository().deleteAll();
	}

	public void deleteAllInBatch() {
		this.getRepository().deleteAllInBatch();
	}

	public void deleteInBatch(Iterable<T> entities) {
		this.getRepository().deleteInBatch(entities);
	}

	public List<T> findAll() {
		return this.getRepository().findAll();
	}

	public List<T> findAll(Map<String, Object> map) {
		Specification<T> spec = DynamicSpecifications.buildSpecification(map,
				this.getRepository().getType());
		return findAll(spec);
	}

	public List<T> findAll(Map<String, Object> map, Sort sort) {
		return findAll(DynamicSpecifications.buildSpecification(map, this
				.getRepository().getType()), sort);
	}

	public List<T> findAll(Iterable<ID> ids) {
		return this.getRepository().findAll(ids);
	}

	public Page<T> findAll(Pageable pageable) {
		return this.getRepository().findAll(pageable);
	}

	public List<T> findAll(Sort sort) {
		return this.getRepository().findAll(sort);
	}

	public List<T> findAll(Specification<T> spec) {
		return this.getRepository().findAll(spec);
	}

	public Page<T> findAll(Specification<T> spec, Pageable pageable) {
		return this.getRepository().findAll(spec, pageable);
	}

	public List<T> findAll(Specification<T> spec, Sort sort) {
		return this.getRepository().findAll(spec, sort);
	}

	public List<T> findAll(int size) {
		return this.getRepository().findAll(size);
	}

	public List<T> findAll(Specification<T> spec, int size) {
		return this.getRepository().findAll(spec, size);
	}

	public List<T> findAll(Specification<T> spec, Sort sort, int size) {
		return this.getRepository().findAll(spec, sort, size);
	}

	public T findOne(ID id) {
		return this.getRepository().findOne(id);
	}

	public T findOne(Specification<T> spec) {
		return this.getRepository().findOne(spec);
	}

	public Page<T> findEntityPage(Map<String, Object> params, int pageNumber,
			int pageSize, String name) {
		return findEntityPage(params, pageNumber, pageSize, name, false);
	}

	public Page<T> findEntityPage(Map<String, Object> params, int pageNumber,
			int pageSize, Sort sort) {
		return findEntityPage(params, pageNumber, pageSize, sort, false);
	}

	public Page<T> findEntityPage(Map<String, Object> params, int pageNumber,
			int pageSize, String name, boolean reload) {
		Sort sort = null;
		String[] names = StringUtils.split(name, "_", 2);
		if (names != null) {
			if (names[0].equalsIgnoreCase("desc")) {
				sort = Sorts.Desc(names[1]);
			} else {
				sort = Sorts.Asc(names.length == 2 ? names[1] : names[0]);
			}
		}
		return findEntityPage(params, pageNumber, pageSize, sort, reload);
	}

	public Page<T> findEntityPage(Map<String, Object> params, int pageNumber, int pageSize, Sort sort, boolean reload) {
		int pageNo = pageNumber - 1;
		PageRequest pageRequest = new PageRequest(pageNo, pageSize, sort);
		Specification<T> spec = DynamicSpecifications.buildSpecification(params, this.getRepository().getType());
		Page<T> page = this.getRepository().findAll(spec, pageRequest);
		
		if (reload && page != null && !page.hasContent() && pageNo > 1) {
			pageRequest = new PageRequest(pageNo - 1, pageSize, sort);
			page = this.getRepository().findAll(spec, pageRequest);
		}
		
		return page;
	}

	public long count() {
		return this.getRepository().count();
	}

	public long count(Specification<T> spec) {
		return this.getRepository().count(spec);
	}

	public <E> List<E> distinct(String name, Class<E> c) {
		return this.getRepository().distinct(name, c);
	}

	/**
	 * @return
	 */
	protected abstract BaseRepository<T, ID> getRepository();

}
