package com.xdragon.utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

public class Sorts {
	private Sorts() {
	}

	public static Sort Desc(String... names) {
		return new Sort(Direction.DESC, names);
	}

	public static Sort Asc(String... names) {
		return new Sort(names);
	}

	public static <T> void sort(List<T> list, final String fieldName, final Direction order) {
		Collections.sort(list, new Comparator<T>() {
			@Override
			public int compare(T o1, T o2) {
				int flag = 0;
				try {
					Object value1 = PropertyUtils.getProperty(o1, fieldName);
					Object value2 = PropertyUtils.getProperty(o2, fieldName);
					if (order != null && order == Direction.DESC) {
						flag = value2.toString().compareTo(value1.toString());
					} else {
						flag = value1.toString().compareTo(value2.toString());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				return flag;
			}
		});
	}
}
