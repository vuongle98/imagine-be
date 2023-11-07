package com.vuongle.imagine.exceptions;

import com.vuongle.imagine.dto.common.ErrorDetails;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Instant;
import java.util.ArrayList;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(DataFormatException.class)
    public ResponseEntity<ErrorDetails> handleDataFormatException(DataFormatException exception, WebRequest webRequest) {
        ErrorDetails error = new ErrorDetails(exception.getMessage(), Instant.now(), webRequest.getDescription(false));

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoPermissionException.class)
    public ResponseEntity<ErrorDetails> handleDataFormatException(NoPermissionException exception, WebRequest webRequest) {
        ErrorDetails error = new ErrorDetails(exception.getMessage(), Instant.now(), webRequest.getDescription(false));

        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleDataNotFoundException(DataNotFoundException exception, WebRequest webRequest) {
        ErrorDetails error = new ErrorDetails(exception.getMessage(), Instant.now(), webRequest.getDescription(false));

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(StorageException.class)
    public ResponseEntity<ErrorDetails> handleStorageException(StorageException exception, WebRequest webRequest) {
        ErrorDetails error = new ErrorDetails(exception.getMessage(), Instant.now(), webRequest.getDescription(false));

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(org.springframework.web.bind.MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        var errors = new ArrayList<>();

//        Map<String, String> errs = new HashMap<>();
//
//        for (var err: ex.getFieldErrors()) {
//            errs.put(err.getField(), err.getDefaultMessage());
//        }

        for (var err : ex.getBindingResult().getAllErrors()) {
            errors.add(err.getDefaultMessage());
        }

        ErrorDetails errorResponse = new ErrorDetails(errors.toString(), Instant.now(), "An error occurred");

        return this.handleExceptionInternal(ex, errorResponse, headers, status, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> globalExceptionHandle(Exception exception, WebRequest webRequest) {
        ErrorDetails error = new ErrorDetails(exception.getMessage(), Instant.now(), webRequest.getDescription(false));

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
