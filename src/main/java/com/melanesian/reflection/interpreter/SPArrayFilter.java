package com.melanesian.reflection.interpreter;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * created by dimasNaresh(thread009)
 */
public class SPArrayFilter extends Expression {

    @SuppressWarnings("unused method. This method call at ExpressionFactory using reflection")
    public SPArrayFilter(Object value, String params) {
        super(value, params);
    }

    @Override
    public Object invokeExpression() throws IllegalAccessException {
        ArrayList<?> value = (ArrayList<?>) getValue();
        IllegalStateException stEx = new IllegalStateException();
        Object filteredValue = value.stream().filter(o -> {
            try {
                String[] splitParamByEquals = getParams()[0].split(ExpressionConstant.EQUALS_STRING);
                Field field = getField(o.getClass(), splitParamByEquals[0].trim());
                field.setAccessible(true);
                return field.get(o).toString().equals(splitParamByEquals[1].trim());
            } catch (IllegalAccessException ex) {
                stEx.setStackTrace(ex.getStackTrace());
                return false;
            }
        }).findAny().orElse(null);

        if (stEx.getStackTrace() == null)
            throw new IllegalAccessException("cannot found field ".concat(getParams()[0]).concat("on array ").concat(value.getClass().getName()));
        else
            return filteredValue;
    }
}
