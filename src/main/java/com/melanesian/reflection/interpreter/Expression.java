package com.melanesian.reflection.interpreter;

/**
 * created by dimasNaresh(thread009)
 */
public abstract class Expression extends ExpressionHelper {

    private Object value;
    private String[] params;

    Expression(Object value, String params) {
        this.params = splittingAllParamsFromExpression(params);
        this.value = value;
    }

    public abstract Object invokeExpression() throws IllegalAccessException;

    String[] getParams() {
        return params;
    }

    Object getValue() {
        return value;
    }

    private String[] splittingAllParamsFromExpression(String expression) {
        return getExpressionParams(expression);
    }
}
