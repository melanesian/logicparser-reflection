package com.melanesian.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public interface Reflection {

	Object getObject(Object bean, String fieldName);

	Field getField(Class<?> clazz, String fieldName);

	Method getMethod(Class<?> claxx, String method);
}
