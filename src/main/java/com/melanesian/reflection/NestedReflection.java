package com.melanesian.reflection;

import com.melanesian.reflection.interpreter.Expression;
import com.melanesian.reflection.interpreter.ExpressionConstant;
import com.melanesian.reflection.interpreter.ExpressionFactory;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;


public class NestedReflection extends ReflectionHelper implements Reflection {

    /**
     * Returns the value of a (nested) field on a bean.
     * NOTE:    This method need to refactor and must be put inside interpreter, so all reflection expression must be
     *          triggered by same pattern
     *
     * @param bean      java bean
     * @param fieldName field name
     * @return java object
     */
    public Object getObject(Object bean, String fieldName) {
        if (bean == null || fieldName == null)
            throw new IllegalStateException(String.format("bean: %s, fieldName: %s", bean, fieldName));

        String[] nestedFields = StringUtils.split(fieldName, ".");
        Class < ? > componentClass = bean.getClass();
        Object value = bean;
        boolean isArray = false;

        try {
            for (String nestedField : nestedFields) {
                Field field;
                if (nestedField.contains(ExpressionConstant.PARENTHESS_OPEN_STRING)) {
                    Method method = getMethod(componentClass, nestedField);
                    value = method.invoke(value, getParameters(nestedField));
                    isArray = false;
                } else if(nestedField.contains(ExpressionConstant.BRACKET_OPEN_STRING)){
                    Expression expression = ExpressionFactory.newInstance().gettingExpression(value, nestedField);
                    value = expression.invokeExpression();
                    isArray = false;
                } else if (isArray) {
                    value = ((List<?>) value).get(Integer.parseInt(nestedField));
                    isArray = false;
                } else {
                    field = getField(componentClass, nestedField);
                    field.setAccessible(true);
                    value = field.get(value);
                    isArray = false;
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
}