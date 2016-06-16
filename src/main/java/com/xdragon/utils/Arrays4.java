package com.xdragon.utils;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.ArrayUtils;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;

public class Arrays4 {

	private Arrays4() {
	}

	@SuppressWarnings("unchecked")
	public static <T> T[] valueOf(Iterator<T> iterator) {
		List<T> list = Lists.newArrayList(iterator);
		return (T[]) list.toArray();
	}

	@SuppressWarnings("unchecked")
	public static <T> T[] valueOf(Iterable<T> Iterable) {
		List<T> list = Lists.newArrayList(Iterable);
		return (T[]) list.toArray();
	}

	/**
	 * 数组过滤
	 * 
	 * @param a
	 * @param predicate
	 * @return
	 */
	public static <T> T[] filter(@NotNull T[] a, @NotNull Predicate<T> predicate) {
		T[] _a = a.clone();
		for (T t : _a) {
			if (!predicate.apply(t)) {
				_a = ArrayUtils.removeElement(_a, t);
			}
		}
		return _a;
	}

	/**
	 * 数组查找
	 * 
	 * @param a
	 * @param predicate
	 * @return
	 */
	public static <T> T find(@NotNull T[] a, @NotNull Predicate<T> predicate) {
		for (T t : a) {
			if (predicate.apply(t)) {
				return t;
			}
		}
		return null;
	}

	/**
	 * 数组删除
	 * 
	 * @param a
	 * @param key
	 * @return
	 */
	public static <T> T[] remove(@NotNull T[] a, Object key) {
		return ArrayUtils.removeElement(a, key);
	}

	/**
	 * 删除数组中所有
	 * 
	 * @param a
	 * @param key
	 * @return
	 */
	public static <T> T[] removeAll(@NotNull T[] a, Object key) {
		T[] _a = a.clone();
		int i = -1;
		while ((i = ArrayUtils.indexOf(_a, key)) >= 0) {
			_a = ArrayUtils.remove(_a, i);
		}
		return _a;
	}

	public static boolean deepEquals(Object[] a1, Object[] a2) {
		if (a1 == a2)
			return true;
		if (a1 == null || a2 == null)
			return false;
		int length = a1.length;
		if (a2.length != length)
			return false;

		for (int i = 0; i < length; i++) {
			Object e1 = a1[i];
			Object e2 = a2[i];

			if (e1 == e2)
				continue;
			if (e1 == null)
				return false;

			// Figure out whether the two elements are equal
			boolean eq = deepEquals0(e1, e2);

			if (!eq)
				return false;
		}
		return true;
	}

	static boolean deepEquals0(Object e1, Object e2) {
		assert e1 != null;
		boolean eq;
		if (e1 instanceof Object[] && e2 instanceof Object[])
			eq = deepEquals((Object[]) e1, (Object[]) e2);
		else if (e1 instanceof byte[] && e2 instanceof byte[])
			eq = Arrays.equals((byte[]) e1, (byte[]) e2);
		else if (e1 instanceof short[] && e2 instanceof short[])
			eq = Arrays.equals((short[]) e1, (short[]) e2);
		else if (e1 instanceof int[] && e2 instanceof int[])
			eq = Arrays.equals((int[]) e1, (int[]) e2);
		else if (e1 instanceof long[] && e2 instanceof long[])
			eq = Arrays.equals((long[]) e1, (long[]) e2);
		else if (e1 instanceof char[] && e2 instanceof char[])
			eq = Arrays.equals((char[]) e1, (char[]) e2);
		else if (e1 instanceof float[] && e2 instanceof float[])
			eq = Arrays.equals((float[]) e1, (float[]) e2);
		else if (e1 instanceof double[] && e2 instanceof double[])
			eq = Arrays.equals((double[]) e1, (double[]) e2);
		else if (e1 instanceof boolean[] && e2 instanceof boolean[])
			eq = Arrays.equals((boolean[]) e1, (boolean[]) e2);
		else
			eq = e1.equals(e2);
		return eq;
	}

}
