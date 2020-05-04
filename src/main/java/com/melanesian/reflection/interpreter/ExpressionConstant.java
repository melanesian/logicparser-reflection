package com.melanesian.reflection.interpreter;

/**
 * created by dimasNaresh(thread009)
 */
class ExpressionConstant {

    private ExpressionConstant() {
        throw new IllegalStateException("This is class for some constant value in melanesian reflection");
    }

    static final String EQUALS_STRING = "=";
    static final String COMMA_STRING = ",";

    static final char BRACKET_OPEN_CHARACTER = '{';
    static final char BRACKET_CLOSED_CHARACTER = '}';

    static final String ERROR_MESSAGE_NO_SUCH_A_EXPRESSION = "Cannot find expression %s at factory";
}
