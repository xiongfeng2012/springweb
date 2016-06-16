package com.xdragon.jpa.repository;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * 扩展的数据库操作接口,继承了JpaRepository和JpaSpecificationExecutor接口
 * 
 * @author chengyadong
 * @version 0.0.7
 * @see {@linkplain org.springframework.data.jpa.repository.JpaRepository
 *      JpaRepository}
 * @see {@linkplain org.springframework.data.jpa.repository.JpaSpecificationExecutor
 *      JpaSpecificationExecutor}
 * @param <T>
 *            数据库映射的实体对象
 * @param <ID>
 *            数据库映射的实体对象的主键类型
 * 
 */
@NoRepositoryBean
public interface BaseRepository <T, ID extends Serializable> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {
	
	public T save(Map<String, Object> map);
	
	/**
	 * 扩展的保存方法
	 * 
	 * @author chengyadong
	 * @param entities
	 *            动态数组对象
	 * @return 保存成功后的实体集合
	 */
	public List<T> save(T... entities);

	/**
	 * 扩展的保存方法
	 * 
	 * @author chengyadong
	 * @param iterator
	 *            迭代器对象
	 * @return 保存成功后的实体集合
	 */
	public List<T> save(Iterator<T> iterator);

	/**
	 * 扩展的批量保存方法
	 * 
	 * @author chengyadong
	 * @param iterable
	 *            对象
	 * @param count
	 *            批量保存时每次提交的数量
	 * @return 保存成功后的实体集合
	 */
	public List<T> batchsave(Iterable<T> iterable, int count);

	/**
	 * 扩展的批量保存方法
	 * 
	 * @author chengyadong
	 * @param iterator
	 *            迭代器对象
	 * @param count
	 *            批量保存时每次提交的数量
	 * @return 保存成功后的实体集合
	 */
	public List<T> batchsave( Iterator<T> iterator, int count);

	/**
	 * 扩展的删除方法
	 * 
	 * @author chengyadong
	 * @param entities
	 *            动态数组对象
	 */
	public void delete(T... entities);

	/**
	 * 扩展的删除方法
	 * 
	 * @author chengyadong
	 * @param entities
	 *            迭代器对象
	 */
	public void delete(Iterator<T> entities);

	/**
	 * 修改方法
	 * 
	 * @author chengyadong
	 * @param t
	 *            需要修改的实体
	 * @return 修改后的实体
	 */
	public T merge(T t);

	/**
	 * 
	 * @param parameters
	 * @param id
	 * @return
	 */
	public int merge(Map<String, Object> parameters, ID id);

	public int merge(Map<String, Object> parameters, Iterator<ID> ids);

	public int merge(String key, Object value, ID id);

	public int merge(String key, Object value, Iterator<ID> ids);

	public int merge(Map<String, Object> map, Specification<T> spec);

	/**
	 * 扩展的查询方法
	 * 
	 * @author chengyadong
	 * @param size
	 *            查询的数量
	 * @return 查询出的对象集合
	 */
	public List<T> findAll(int size);

	public List<T> findAll(Specification<T> spec, int size);

	public List<T> findAll(Specification<T> spec, Sort sort, int size);

	public <C> List<C> distinct(String name, Class<C> c);

	public Class<T> getType();

}
