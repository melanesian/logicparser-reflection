package com.melanesian.reflection;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;


public class MelanesianReflection implements Reflection{

    /**
     * Returns the value of a (nested) field on a bean.
     *
     * @param bean java bean
     * @param fieldName field name
     * @return java object
     */
    public Object getObject(Object bean, String fieldName) {
        String[] nestedFields = StringUtils.split(fieldName, ".");
        Class < ? > componentClass = bean.getClass();
        Object value = bean;
        boolean isArray = false;

        try {
            for (String nestedField : nestedFields) {
                Field field;
                if (nestedField.contains("(")) {
                    Method method = getMethod(componentClass, nestedField);
                    value = method.invoke(value, getMethodValue(nestedField));
                } else if (isArray && value != null) {
                    value = ((List<?>) value).get(Integer.parseInt(nestedField));
                    isArray = false;
                } else {
                    field = getField(componentClass, nestedField);
                    field.setAccessible(true);
                    value = field.get(value);
                }

                if (value != null) {
                    componentClass = value.getClass();
                    if (value instanceof List<?>) {
                        isArray = true;
                    }
                }
            }
        } catch (ClassCastException | InvocationTargetException | IllegalAccessException iae) {
            throw new IllegalStateException(iae);
        }

        return value;

    }

    /**
     * Get the specified field on the class. If the field is not found on the class itself will recursively check
     * the superclass.
     *
     * @param clazz object class
     * @param fieldName field name
     * @return java.lang.reflect.Field
     */
    public Field getField(Class<?> clazz, String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException nsf) {
            if (clazz.getSuperclass() != null) {
                return getField(clazz.getSuperclass(), fieldName);
            }
            throw new IllegalStateException("failed to get field");
        }
    }

    /**
     *
     * @param claxx
     * @param method
     * @return
     */
    public Method getMethod(Class<?> claxx, String method) {
        try {
            String methodName = method.replaceAll("\\([^)]*\\)", "");
            Class<?>[] parameters = getParameterTypes(method);

            return claxx.getDeclaredMethod(methodName, parameters);
        } catch (NoSuchMethodException nsf) {
            if (claxx.getSuperclass() != null) {
                return getMethod(claxx.getSuperclass(), method);
            }
            throw new IllegalStateException("failed to get method");
        }
    }

    /**
     *
     * @param method
     * @return
     */
    private Object[] getMethodValue(String method) {
        String ex = method;

        ex = ex.substring(ex.indexOf("(") + 1);
        ex = ex.substring(0, ex.indexOf(")"));

        return ex.isEmpty() ? new Object[0]:ex.split(",");
    }

    /**
     *
     * @param method
     * @return
     */
    private Class<?>[] getParameterTypes(String method) {
        Object[] values = getMethodValue(method);
        Class<?>[] classes = new Class[values.length];

        for (int i=0; i<values.length; i++) {
            if (!StringUtils.isNumeric(values[i].toString()))
                classes[i] = Object.class;
            else if (StringUtils.isNumeric(values[i].toString()))
                classes[i] = Integer.class;
        }
        return classes;
    }
}
