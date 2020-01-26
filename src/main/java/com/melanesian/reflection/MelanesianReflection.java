package com.melanesian.reflection;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.List;


public class MelanesianReflection {

    /**
     * Returns the value of a (nested) field on a bean.
     *
     * @param bean java bean
     * @param fieldName field name
     * @return java object
     */
    public Object get(Object bean, String fieldName) {
        String[] nestedFields = StringUtils.split(fieldName, ".");
        Class < ? > componentClass = bean.getClass();
        Object value = bean;
        boolean isArray = false;

        try {
            for (String nestedField : nestedFields) {
                Field field;
                if (isArray) {
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
        } catch (IllegalAccessException iae) {
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
    public Field getField(Class < ? > clazz, String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException nsf) {
            if (clazz.getSuperclass() != null) {
                return getField(clazz.getSuperclass(), fieldName);
            }
            throw new IllegalStateException("failed to get field");
        }
    }
}
