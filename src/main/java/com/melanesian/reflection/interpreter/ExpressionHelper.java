package com.melanesian.reflection.interpreter;

import java.lang.reflect.Field;

/**
 * created by dimasNaresh(thread009)
 */
class ExpressionHelper {

    /**
     * Getting expression name
     *
     * @param expression full expression
     * @return String of expression name
     */
    String getExpressionName(String expression) {
        return expression.replaceAll("\\{[^)]*\\}", "");
    }

    /**
     * Getting expression parameters
     *
     * @param expression full expression
     * @return Array of String parameters
     */
    String[] getExpressionParams(String expression) {
        String ex = expression;

        ex = ex.substring(ex.indexOf(ExpressionConstant.BRACKET_OPEN_CHARACTER) + 1);
        ex = ex.substring(0, ex.indexOf(ExpressionConstant.BRACKET_CLOSED_CHARACTER));

        return ex.split(ExpressionConstant.COMMA_STRING);
    }

    /**
     * Get the specified field on the class. If the field is not found on the class itself will recursively check
     * the superclass.
     *
     * @param clazz object class
     * @param fieldName field name
     * @return java.lang.reflect.Field
     */
    Field getField(Class<?> clazz, String fieldName) {
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
