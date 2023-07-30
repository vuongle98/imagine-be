package com.vuongle.imagine.exceptions;

import com.vuongle.imagine.dto.common.MessageResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(DataFormatException.class)
    public ResponseEntity<Object> handleDataFormatException(DataFormatException dataFormatException, WebRequest webRequest) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        MessageResponse errorResponse = new MessageResponse("An error occurred", dataFormatException.getMessage());
        return handleExceptionInternal(dataFormatException, errorResponse, new HttpHeaders(), status, webRequest);
    }

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<Object> handleDataNotFoundException(DataNotFoundException dataNotFoundException, WebRequest webRequest) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        MessageResponse errorResponse = new MessageResponse("An error occurred", dataNotFoundException.getMessage());
        return handleExceptionInternal(dataNotFoundException, errorResponse, new HttpHeaders(), status, webRequest);
    }

    @ExceptionHandler(StorageException.class)
    public ResponseEntity<Object> handleDataNotFoundException(StorageException storageException, WebRequest webRequest) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        MessageResponse errorResponse = new MessageResponse("An error occurred", storageException.getMessage());
        return handleExceptionInternal(storageException, errorResponse, new HttpHeaders(), status, webRequest);
    }
}
