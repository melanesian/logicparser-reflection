package com.melanesian.reflection.interpreter;

import com.melanesian.reflection.ReflectionHelper;

/**
 * created by dimasNaresh(thread009)
 */
public abstract class Expression extends ReflectionHelper {

    private Object value;
    private String[] params;

    Expression(Object value, String expression) {
        this.params = getExpressionParams(expression);
        this.value = value;
    }

    public abstract Object invokeExpression() throws IllegalAccessException;

    String[] getParams() {
        return params;
    }

    Object getValue() {
        return value;
    }
}
