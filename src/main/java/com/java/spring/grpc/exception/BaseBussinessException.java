package com.java.spring.grpc.exception;

import io.grpc.Status;

public abstract class BaseBussinessException extends RuntimeException {

    protected BaseBussinessException(String message) {
        super(message);
    }

    public abstract Status getCode();
    public abstract String getMessage();
}
