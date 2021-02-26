package com.technocorp.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestControllerAdvice
public class RestExceptionHandler {


    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<StandardError> handleResponseStatusException(ResponseStatusException e) {
        return ResponseEntity.status(e.getStatus())
                .headers(HttpHeaders.EMPTY)
                .body(StandardError.builder()
                        .status(e.getRawStatusCode())
                        .message(e.getMessage())
                        .build());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public StandardError handleGeneralExceptions(Exception e) {
        return StandardError.builder()
                .status(INTERNAL_SERVER_ERROR.value())
                .message(e.getMessage())
                .build();
    }
    

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public StandardError handleRuntimeExceptions(RuntimeException e) {
        return StandardError.builder()
                .status(INTERNAL_SERVER_ERROR.value())
                .message("An unexpected error ocurred")
                .build();
    }

    @ExceptionHandler(IOException.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public StandardError handleIoExceptionExceptions(IOException e) {
        return StandardError.builder()
                .status(INTERNAL_SERVER_ERROR.value())
                .message("Input/Output error")
                .build();
    }

}
