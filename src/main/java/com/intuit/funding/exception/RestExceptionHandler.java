package com.intuit.funding.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {CustomException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    protected Message errorHandler(CustomException exception, WebRequest req){
        Message message = new Message(exception.getMessage(), "alert-danger");
        message.setErrorCode("CA101");
        return message;
    }
}
