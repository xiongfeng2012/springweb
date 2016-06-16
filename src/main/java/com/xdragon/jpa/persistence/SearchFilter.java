package com.xdragon.jpa.persistence;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Maps;

public class SearchFilter {

	public static enum Operator {
		EQ, LIKE, GT, LT, GTE, LTE, NE, NULL, NOTNULL, IN, NOTIN, BETWEEN
	}

	public String fieldName;
	public Object value;
	public Operator operator;

	public SearchFilter(String fieldName, Operator operator, Object value) {
		this.fieldName = fieldName;
		this.value = value;
		this.operator = operator;
	}

	/**
	 * searchParams中key的格式为OPERATOR_FIELDNAME
	 */
	public static Map<String, SearchFilter> parse(Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = Maps.newHashMap();
		if (searchParams == null || searchParams.size() == 0)
			return filters;
		for (Entry<String, Object> entry : searchParams.entrySet()) {
			// 过滤掉空值
			String key = entry.getKey();
			String[] names = StringUtils.split(key, "_", 2);
			if (names == null) {
				throw new IllegalArgumentException(key + " is not a valid search filter name");
			}
			if (names.length == 1) {
				names = ArrayUtils.add(names, 0, "EQ");
			}
			// 操作符与大小写无关
			Operator operator = Operator.valueOf(names[0].toUpperCase());
			Object value = entry.getValue();
			if (operator != Operator.NULL && operator != Operator.NOTNULL) {
				if (value == null || (value instanceof String && StringUtils.isBlank((String) value))) {
					continue;
				}
			}

			if (operator == Operator.BETWEEN) {
				if (!value.getClass().isArray()) {
					continue;
				}
			}

			// 拆分operator与filedAttribute

			String filedName = names[1];
			// 创建searchFilter
			SearchFilter filter = new SearchFilter(filedName, operator, value);
			filters.put(key, filter);
		}

		return filters;
	}
}
