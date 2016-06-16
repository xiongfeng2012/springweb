package com.xdragon.jpa.persistence;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jodd.bean.BeanUtil;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.jpa.domain.Specification;

import com.xdragon.annotation.DateFormat;
import com.xdragon.annotation.Trim;
import com.xdragon.utils.Arrays4;
import com.xdragon.utils.ClassUtils;
import com.xdragon.utils.Collections3;

import com.google.common.collect.Lists;

public class DynamicSpecifications {

	public static <T> Specification<T> buildSpecification(Map<String, Object> searchParams, Class<T> cla) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<T> spec = bySearchFilter(filters.values(), cla);
		return spec;
	}

	public static <T> Specification<T> bySearchFilter(final Collection<SearchFilter> filters,
			final Class<T> entityClazz) {
		if (Collections3.isEmpty(filters))
			return null;
		return new SpecificationImpl<T>(filters, entityClazz);
	}

	public static class SpecificationImpl<T> implements Specification<T> {

		private Collection<SearchFilter> filters;
		private Class<T> clazz;

		public SpecificationImpl(Collection<SearchFilter> filters, Class<T> clazz) {
			this.filters = filters;
			this.clazz = clazz;
		}

		@SuppressWarnings({ "unchecked", "rawtypes" })
		@Override
		public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
			if (Collections3.isNotEmpty(filters) && clazz != null) {
				List<Predicate> predicates = Lists.newArrayListWithCapacity(filters.size());

				for (Iterator<SearchFilter> it = filters.iterator(); it.hasNext();) {
					SearchFilter filter = it.next();
					String[] names = StringUtils.split(filter.fieldName, ".");
					boolean flag = false;
					try {
						//change the beanutil installer
						flag = BeanUtil.silent.hasRootProperty(clazz.newInstance(), filter.fieldName);
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (!flag) {
						continue;
					}
					Path<String> expression = root.get(names[0]);
					for (int i = 1; i < names.length; i++) {
						expression = expression.get(names[i]);
					}
					if (filter.value != null) {

						if (filter.value instanceof String) {
							String value = (String) filter.value;
							boolean isnumber = false;
							Method method = ClassUtils.getMethodCascade(clazz, filter.fieldName);
							Class<?> c = method.getReturnType();
							if (c.isAssignableFrom(String.class)) {
								Trim trim = AnnotationUtils.findAnnotation(method, Trim.class);
								if (trim != null) {
									filter.value = StringUtils.trim(value);
								}
							} else if (isnumber = NumberUtils.isNumber(value) && c.isAssignableFrom(Short.class)) {
								filter.value = Short.parseShort(value);
							} else if (isnumber && c.isAssignableFrom(Integer.class)) {
								filter.value = Integer.parseInt(value);
							} else if (c.isAssignableFrom(Calendar.class)) {
								filter.value = DateUtils.toCalendar(parseDate(method, value));
							} else if (c.isAssignableFrom(Date.class)) {
								filter.value = parseDate(method, value);
							}
						} else if (filter.value instanceof Object[]) {

						}
					}
					// logic operator
					switch (filter.operator) {
					case EQ:
						predicates.add(builder.equal(expression, filter.value));
						break;
					case LIKE:
						predicates.add(builder.like(expression, "%" + filter.value + "%"));
						break;
					case GT:
						predicates.add(builder.greaterThan(expression, (Comparable) filter.value));
						break;
					case LT:
						predicates.add(builder.lessThan(expression, (Comparable) filter.value));
						break;
					case GTE:
						predicates.add(builder.greaterThanOrEqualTo(expression, (Comparable) filter.value));
						break;
					case LTE:
						predicates.add(builder.lessThanOrEqualTo(expression, (Comparable) filter.value));
						break;
					case NE:
						predicates.add(builder.notEqual(expression, filter.value));
						break;
					case NULL:
						predicates.add(builder.isNull(expression));
						break;
					case NOTNULL:
						predicates.add(builder.isNotNull(expression));
						break;
					case IN:
						Object[] objs = null;
						if (filter.value instanceof Iterator) {
							objs = Arrays4.valueOf((Iterator) filter.value);
						} else if (filter.value instanceof Iterable) {
							objs = Arrays4.valueOf((Iterable) filter.value);
						} else if (filter.value instanceof Object[]) {
							objs = (Object[]) filter.value;
						}
						predicates.add(expression.in(objs));
						break;
					case BETWEEN:
						// predicates.add(builder.between(expression));
						break;
					default:
						break;
					}

				}
				// 将所有条件用 and 联合起来
				if (predicates.size() > 0) {
					return builder.and(predicates.toArray(new Predicate[predicates.size()]));
				}
			}
			return builder.conjunction();
		}

		private Date parseDate(Method method, String value) {
			DateFormat dateType = AnnotationUtils.findAnnotation(method, DateFormat.class);
			if (dateType != null) {
				try {
					return DateUtils.parseDate(value, dateType.value());
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			return null;
		}

	}

}
