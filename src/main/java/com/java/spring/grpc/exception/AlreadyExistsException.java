package com.java.spring.grpc.exception;

import io.grpc.Status;

public class AlreadyExistsException extends BaseBussinessException {

    private static final String ERROR_MESSAGE = "Item '%s' já está cadastrado no sistema.";
    private final String name;

    public AlreadyExistsException(String name) {
        super(String.format(ERROR_MESSAGE, name));
        this.name = name;
    }

    @Override
    public Status getCode() {
        return Status.ALREADY_EXISTS;
    }

    @Override
    public String getMessage() {
        return String.format(ERROR_MESSAGE, this.name);
    }

}
