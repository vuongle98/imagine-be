package com.vuongle.imagine.exceptions;

import com.vuongle.imagine.dto.common.MessageResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(DataFormatException.class)
    public ResponseEntity<Object> handleDataFormatException(DataFormatException dataFormatException, WebRequest webRequest) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        MessageResponse errorResponse = new MessageResponse("An error occurred", dataFormatException.getMessage(), HttpStatus.BAD_REQUEST);
        return handleExceptionInternal(dataFormatException, errorResponse, new HttpHeaders(), status, webRequest);
    }

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<Object> handleDataNotFoundException(DataNotFoundException dataNotFoundException, WebRequest webRequest) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        MessageResponse errorResponse = new MessageResponse("An error occurred", dataNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        return handleExceptionInternal(dataNotFoundException, errorResponse, new HttpHeaders(), status, webRequest);
    }

    @ExceptionHandler(StorageException.class)
    public ResponseEntity<Object> handleDataNotFoundException(StorageException storageException, WebRequest webRequest) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        MessageResponse errorResponse = new MessageResponse("An error occurred", storageException.getMessage(), HttpStatus.BAD_REQUEST);
        return handleExceptionInternal(storageException, errorResponse, new HttpHeaders(), status, webRequest);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(org.springframework.web.bind.MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        var errors = new ArrayList<>();
        for (var err : ex.getBindingResult().getAllErrors())
            errors.add(err.getDefaultMessage());

        MessageResponse errorResponse = new MessageResponse("An error occurred", errors.toString(), HttpStatus.BAD_REQUEST);

        return this.handleExceptionInternal(ex, errorResponse, headers, status, request);
    }
}
