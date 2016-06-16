package com.xdragon.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotNull;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.xdragon.function.UnaryOperator;

/**
 * Collections工具集.
 * 
 * 在JDK的Collections和Guava的Collections2后, 命名为Collections3.
 * 
 * 函数主要由两部分组成，一是自反射提取元素的功能，二是源自Apache Commons Collection, 争取不用在项目里引入它。
 * 
 */
public class Collections3 {
	
	/**
	 * 提取集合中的对象的两个属性(通过Getter函数), 组合成Map.
	 * 
	 * @param collection
	 *            来源集合.
	 * @param keyPropertyName
	 *            要提取为Map中的Key值的属性名.
	 * @param valuePropertyName
	 *            要提取为Map中的Value值的属性名.
	 */
	public static Map<Object, Object> extractToMap(
			final Collection<?> collection, final String keyPropertyName,
			final String valuePropertyName) {
		Map<Object, Object> map = new HashMap<Object, Object>(collection.size());

		try {
			for (Object obj : collection) {
				map.put(PropertyUtils.getProperty(obj, keyPropertyName),
						PropertyUtils.getProperty(obj, valuePropertyName));
			}
		} catch (Exception e) {
			throw Reflections.convertReflectionExceptionToUnchecked(e);
		}

		return map;
	}

	/**
	 * 提取集合中的对象的一个属性(通过Getter函数), 组合成List.
	 * 
	 * @param collection
	 *            来源集合.
	 * @param propertyName
	 *            要提取的属性名.
	 */
	public static List<Object> extractToList(final Collection<?> collection,
			final String propertyName) {
		List<Object> list = new ArrayList<Object>(collection.size());

		try {
			for (Object obj : collection) {
				list.add(PropertyUtils.getProperty(obj, propertyName));
			}
		} catch (Exception e) {
			throw Reflections.convertReflectionExceptionToUnchecked(e);
		}

		return list;
	}

	/**
	 * 提取集合中的对象的一个属性(通过Getter函数), 组合成由分割符分隔的字符串.
	 * 
	 * @param collection
	 *            来源集合.
	 * @param propertyName
	 *            要提取的属性名.
	 * @param separator
	 *            分隔符.
	 */
	public static String extractToString(final Collection<?> collection,
			final String propertyName, final String separator) {
		List<?> list = extractToList(collection, propertyName);
		return StringUtils.join(list, separator);
	}

	/**
	 * 转换Collection所有元素(通过toString())为String, 中间以 separator分隔。
	 */
	public static String convertToString(final Collection<?> collection,
			final String separator) {
		return StringUtils.join(collection, separator);
	}

	/**
	 * 转换Collection所有元素(通过toString())为String,
	 * 每个元素的前面加入prefix，后面加入postfix，如<div>mymessage</div>。
	 */
	public static String convertToString(final Collection<?> collection,
			final String prefix, final String postfix) {
		StringBuilder builder = new StringBuilder();
		for (Object o : collection) {
			builder.append(prefix).append(o).append(postfix);
		}
		return builder.toString();
	}

	/**
	 * 判断是否为空.
	 */
	public static boolean isEmpty(Collection<?> collection) {
		return collection == null || collection.isEmpty();
	}

	/**
	 * 判断是否为空.
	 */
	public static boolean isNotEmpty(Collection<?> collection) {
		return !isEmpty(collection);
	}

	/**
	 * 取得Collection的第一个元素，如果collection为空返回null.
	 */
	public static <T> T getFirst(Collection<T> collection) {
		if (isEmpty(collection)) {
			return null;
		}

		return collection.iterator().next();
	}

	/**
	 * 获取Collection的最后一个元素 ，如果collection为空返回null.
	 */
	public static <T> T getLast(Collection<T> collection) {
		if (isEmpty(collection)) {
			return null;
		}

		// 当类型为List时，直接取得最后一个元素 。
		if (collection instanceof List) {
			List<T> list = (List<T>) collection;
			return list.get(list.size() - 1);
		}

		// 其他类型通过iterator滚动到最后一个元素.
		Iterator<T> iterator = collection.iterator();
		while (true) {
			T current = iterator.next();
			if (!iterator.hasNext()) {
				return current;
			}
		}
	}

	/**
	 * 返回a+b的新List.
	 */
	public static <T> List<T> union(final Collection<T> a, final Collection<T> b) {
		List<T> result = new ArrayList<T>(a);
		result.addAll(b);
		return result;
	}

	/**
	 * 返回a-b的新List.
	 */
	public static <T> List<T> subtract(final Collection<T> a,
			final Collection<T> b) {
		List<T> list = new ArrayList<T>(a);
		for (Iterator<T> it = a.iterator(); it.hasNext();) {
			list.remove(it.next());
		}
		return list;
	}

	/**
	 * 返回a与b的交集的新List.
	 */
	public static <T> List<T> intersection(Collection<T> a, Collection<T> b) {
		List<T> list = new ArrayList<T>();
		Object[] array = a.toArray();
		Arrays.sort(array);
		for (final T t : b) {
			if (Arrays.binarySearch(array, t) >= 0) {
				list.add(t);
			}
		}
		return list;
	}

	public static <E, T extends Collection<E>> T filter(T t,
			final Predicate<? super E> predicate) {
		if (t != null && predicate != null) {
			for (final Iterator<E> it = t.iterator(); it.hasNext();) {
				if (!predicate.apply(it.next())) {
					it.remove();
				}
			}
		}
		return t;
	}

	public static <E> E find(final Collection<E> collection,
			final Predicate<? super E> predicate) {
		if (collection != null && predicate != null) {
			for (final Iterator<E> it = collection.iterator(); it.hasNext();) {
				E e = it.next();
				if (predicate.apply(e)) {
					return e;
				}
			}
		}
		return null;
	}

	public static <E> int size(Collection<E> collection) {
		return collection == null ? 0 : collection.size();
	}

	public static <E, T extends Collection<? extends E>> T remove(T t, E e) {
		if (t != null) {
			t.remove(e);
		}
		return t;
	}

	public static <E, T extends Collection<? extends E>> T removeAll(final T t,
			E e) {
		if (t != null) {
			boolean f = true;
			while (f) {
				f = t.remove(e);
			}
		}
		return t;
	}

	public static <E> void add(Collection<E> collection,
			Predicate<E> predicate, E e) {
		if (collection == null || predicate == null)
			return;
		if (predicate.apply(e)) {
			collection.add(e);
		}
	}

	public static <E> void replaceAll(List<E> list,
			@NotNull UnaryOperator<E> operator) {
		if (list == null)
			return;
		Objects2.requireNonNull(operator);
		final ListIterator<E> li = list.listIterator();
		while (li.hasNext()) {
			li.set(operator.apply(li.next()));
		}
	}

	public static <S, T> List<T> transform(Collection<S> collection,
			final Function<S, T> function) {
		if (collection == null || function == null)
			return null;
		List<T> list = Lists.newArrayList();
		for (Iterator<S> it = collection.iterator(); it.hasNext();) {
			list.add(function.apply(it.next()));
		}
		return list;
	}

}
