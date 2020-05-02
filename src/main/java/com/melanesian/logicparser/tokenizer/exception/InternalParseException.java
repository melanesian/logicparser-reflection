package com.melanesian.logicparser.tokenizer.exception;

public class InternalParseException extends RuntimeException {
    public InternalParseException(SpelParseException cause) {
        super(cause);
    }

    public SpelParseException getCause() {
        return (SpelParseException)super.getCause();
    }
}
