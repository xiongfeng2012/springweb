package com.xdragon.utils;

import java.util.Arrays;
import java.util.Comparator;

public class Objects2 {

	private Objects2() {
		throw new AssertionError("No com.ty.springexpand.core.utils.Objects instances for you!");
	}

	public static boolean equals(Object a, Object b) {
		return (a == b) || (a != null && a.equals(b));
	}

	public static boolean deepEquals(Object a, Object b) {
		if (a == b)
			return true;
		else if (a == null || b == null)
			return false;
		else
			return Arrays4.deepEquals0(a, b);
	}

	public static int hashCode(Object o) {
		return o != null ? o.hashCode() : 0;
	}

	public static int hash(Object... values) {
		return Arrays.hashCode(values);
	}

	public static String toString(Object o) {
		return String.valueOf(o);
	}

	public static String toString(Object o, String nullDefault) {
		return (o != null) ? o.toString() : nullDefault;
	}

	public static <T> int compare(T a, T b, Comparator<? super T> c) {
		return (a == b) ? 0 : c.compare(a, b);
	}

	public static <T> T requireNonNull(T obj) {
		if (obj == null)
			throw new NullPointerException();
		return obj;
	}

	public static <T> T requireNonNull(T obj, String message) {
		if (obj == null)
			throw new NullPointerException(message);
		return obj;
	}

}
