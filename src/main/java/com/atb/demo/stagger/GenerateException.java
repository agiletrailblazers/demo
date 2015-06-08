package com.atb.demo.stagger;

public class GenerateException extends Throwable {
    public GenerateException(String errorMessage) {
        super(errorMessage);
    }

    public GenerateException(Exception e) {
        super(e);
    }
}
