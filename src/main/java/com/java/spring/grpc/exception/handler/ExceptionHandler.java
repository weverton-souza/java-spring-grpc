package com.java.spring.grpc.exception.handler;

import com.java.spring.grpc.exception.BaseBussinessException;
import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;

@GrpcAdvice
public class ExceptionHandler {

    @GrpcExceptionHandler(BaseBussinessException.class)
    public StatusRuntimeException handleBussinessException(BaseBussinessException e) {
        return e.getCode().withCause(e.getCause()).withDescription(e.getMessage()).asRuntimeException();
    }
}
