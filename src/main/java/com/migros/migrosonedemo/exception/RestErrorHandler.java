package com.migros.migrosonedemo.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
@RequiredArgsConstructor
public class RestErrorHandler {

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(BAD_REQUEST)
    public ValidationErrorResponse handleValidationError(ValidationException ex) {
        return new ValidationErrorResponse("One or more valid errors occured.", BAD_REQUEST.value(), ex.getMessage());
    }
}
