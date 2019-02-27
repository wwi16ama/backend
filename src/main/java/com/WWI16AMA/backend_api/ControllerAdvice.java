package com.WWI16AMA.backend_api;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler({
            ConstraintViolationException.class,
            HttpMessageNotReadableException.class,
            IllegalArgumentException.class,
            PropertyReferenceException.class,
            TransactionSystemException.class})
    protected ResponseEntity<ErrorInfo> handleBadRequest(HttpServletRequest req, Exception ex) {
        return new ResponseEntity<>(new ErrorInfo(req, ex), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchElementException.class)
    protected ResponseEntity<ErrorInfo> handleNoEntryFound(HttpServletRequest req, Exception ex) {
        return new ResponseEntity<>(new ErrorInfo(req, ex), HttpStatus.NOT_FOUND);
    }

}