package com.java.spring.grpc.exception;

import io.grpc.Status;

public class NotFoundException extends BaseBussinessException {

    private static final String ERROR_MESSAGE = "Item com ID '%s' não encontrado.";
    private final long id;

    public NotFoundException(Long id) {
        super(String.format(ERROR_MESSAGE, id));
        this.id = id;
    }

    @Override
    public Status getCode() {
        return Status.NOT_FOUND;
    }

    @Override
    public String getMessage() {
        return String.format(ERROR_MESSAGE, this.id);
    }
    
}
