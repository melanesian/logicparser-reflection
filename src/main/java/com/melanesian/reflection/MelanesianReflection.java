package com.melanesian.reflection;

import com.melanesian.reflection.interpreter.Expression;
import com.melanesian.reflection.interpreter.ExpressionFactory;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;


public class MelanesianReflection implements Reflection{

    private DataType[] types = new DataType[]{
            DataType.INTEGER,
            DataType.DOUBLE,
            DataType.LONG,
            DataType.STRING,
            DataType.BOOLEAN
    };

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
                if (nestedField.contains("(")) {
                    Method method = getMethod(componentClass, nestedField);
                    value = method.invoke(value, getParameters(nestedField));
                    isArray = false;
                } else if(nestedField.contains("{")){
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

    /**
     * Get the specified field on the class. If the field is not found on the class itself will recursively check
     * the superclass.
     * NOTE:    Move to ExpressionHelper
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
     * Get the specified method on the class. If the method is not found on the class
     * itself will recursively check the superclass.
     * NOTE:    Move to ExpressionHelper
     *
     * @param claxx object class
     * @param method method name
     * @return java.lang.reflect.Method
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
     * Returning parameter types of method
     * NOTE:    Move to ExpressionHelper
     *
     * @param method String of method
     * @return array of parameter types
     */
    private Class<?>[] getParameterTypes(String method) {
        Object[] values = getParameters(method);
        Class<?>[] classes = new Class[values.length];

        for (int i=0; i<values.length; i++) {
            if (values[i] instanceof Boolean)
                classes[i] = boolean.class;
            else if (values[i] instanceof Integer)
                classes[i] = int.class;
            else if (values[i] instanceof Double)
                classes[i] = Double.class;
            else if (values[i] instanceof Long)
                classes[i] = Long.class;
            else if (values[i] instanceof String)
                classes[i] = String.class;
        }
        return classes;
    }

    /**
     * Generate method parameter values
     * NOTE:    Move to ExpressionHelper
     *
     * @param method method initialize
     * @return array of java.lang.Object
     */
    private Object[] getParameters(String method) {
        String ex = method;

        ex = ex.substring(ex.indexOf('(') + 1);
        ex = ex.substring(0, ex.indexOf(')'));

        String[] valueSplitter = ex.split(",");

        return ex.isEmpty() ? new Object[0] :
                convertSuggestedType(new Object[valueSplitter.length],valueSplitter,0,0);
    }

    /**
     * checking all values and convert to suggested data type
     * NOTE:    Move to ExpressionHelper
     *
     * @param results array of objects
     * @param values method parameter values
     * @param point starting point
     * @param type data type index
     * @return array of java.lang.Object
     */
    private Object[] convertSuggestedType(Object[] results, String[] values, int point, int type) {
        try {
            if (values[point].trim().equals("true") || values[point].trim().equals("false")) {
                results[point] = Boolean.parseBoolean(values[point].trim());
            } else {
                switch (types[type]){
                    case INTEGER:
                        results[point] = Integer.parseInt(values[point].trim());
                        break;
                    case DOUBLE:
                        results[point] = Double.parseDouble(values[point].trim());
                        break;
                    case LONG:
                        results[point] = Long.valueOf(values[point].trim());
                        break;
                    case STRING:
                        results[point] = String.valueOf(values[point]);
                        break;
                    default:
                        throw new IllegalStateException("cannot found best data type for ");
                }
            }
            if (point == (values.length - 1))
                return results;
            else {
                point++;
                return convertSuggestedType(results, values, point, 0);
            }
        } catch (Exception e) {
            type++;
            return convertSuggestedType(results, values, point, type);
        }
    }
}
