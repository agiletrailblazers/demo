package com.urbn.stagger;

public class GenerateException extends Throwable {
    public GenerateException(String errorMessage) {
        super(errorMessage);
    }

    public GenerateException(Exception e) {
        super(e);
    }
}
