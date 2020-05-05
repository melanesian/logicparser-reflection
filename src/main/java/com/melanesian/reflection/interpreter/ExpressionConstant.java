package com.melanesian.reflection.interpreter;

/**
 * created by dimasNaresh(thread009)
 */
public class ExpressionConstant {

    private ExpressionConstant() {
        throw new IllegalStateException("This is class for some constant value in melanesian reflection");
    }

    static final String EQUALS_STRING = "=";
    public static final String COMMA_STRING = ",";

    public static final char BRACKET_OPEN_CHARACTER = '{';
    public static final char BRACKET_CLOSED_CHARACTER = '}';

    public static final char PARENTHESS_OPEN_CHARACTER = '(';
    public static final char PARENTHESS_CLOSED_CHARACTER = ')';

    public static final String BRACKET_OPEN_STRING = "{";
    public static final String BRACKET_CLOSED_STRING = "}";

    public static final String PARENTHESS_OPEN_STRING = "(";
    public static final String PARENTHESS_CLOSED_STRING = ")";

    static final String ERROR_MESSAGE_NO_SUCH_A_EXPRESSION = "Cannot find expression %s at factory";
}