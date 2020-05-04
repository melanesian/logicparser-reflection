package com.melanesian.reflection.interpreter;

/**
 * created by dimasNaresh(thread009)
 */
public abstract class Expression extends ExpressionHelper {

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
