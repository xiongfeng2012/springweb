package com.xdragon.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.apache.commons.lang3.StringUtils;

public class ClassUtils extends org.springframework.util.ClassUtils {

	public static <T> Field getField(Class<T> c, String fieldName) {
		Field field = null;
		try {
			field = c.getDeclaredField(fieldName);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
		return field;
	}

	public static <T> String getFieldTypeName(Class<T> clazz, String fieldName) {
		Class<?> c = getFieldType(clazz, fieldName);
		return c == null ? null : c.getName();
	}

	public static <T> Class<?> getFieldType(Class<T> c, String fieldName) {
		Field field = getField(c, fieldName);
		return field == null ? null : field.getType();
	}

	public static <T> Field getFieldCascade(Class<T> clazz, String name) {
		String[] names = StringUtils.split(name, '.');
		Class<?> c = clazz;
		Field field = null;
		for (String n : names) {
			field = getField(c, n);
			c = field.getType();
		}
		return field;
	}

	public static <T> Method getMethodCascade(Class<T> clazz, String name) {
		String[] names = StringUtils.split(name, '.');
		Class<?> c = clazz;
		Method method = null;
		for (String n : names) {
			method = getMethod(c, getMethodName("get", n));
			c = method.getReturnType();
		}
		return method;
	}

	public static String getMethodName(String pre, String fieldname) {
		return pre + StringUtils.capitalize(fieldname);
	}

	@SuppressWarnings("unchecked")
	public static <T> T newInstance(Class<?> c) {
		if (c == null)
			return null;
		try {
			return (T) c.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean isPrimitive(Object bean) {
		return bean.getClass().isPrimitive();
	}

}