package com.melanesian.reflection.interpreter;

import com.melanesian.reflection.ReflectionHelper;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * created by dimasNaresh(thread009)
 */
public class ExpressionFactory extends ReflectionHelper {


    public  Expression gettingExpression(Object object, String expression) {
        String expressionName = getExpressionName(expression);
        try {
            Class expressionClass = Class.forName("com.melanesian.reflection.interpreter.".concat(expressionName));
            Constructor expressionConstructor = expressionClass.getConstructor(Object.class, String.class);
            return (Expression) expressionConstructor.newInstance(object, expression);
        } catch (ClassNotFoundException clNFoundEx) {
            throw new IllegalStateException(String.format(ExpressionConstant.ERROR_MESSAGE_NO_SUCH_A_EXPRESSION, expressionName));
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException nSuchMethodEx) {
            throw new IllegalStateException(nSuchMethodEx.getCause());
        }
    }

    public static ExpressionFactory newInstance() {
        return new ExpressionFactory();
    }


}
