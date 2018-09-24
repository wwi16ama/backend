package com.WWI16AMA.backend_api.Member;

import com.WWI16AMA.backend_api.ErrorInfo;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.NoSuchElementException;

@ControllerAdvice
public class MemberControllerAdvise {



    @ExceptionHandler({ConstraintViolationException.class, HttpMessageNotReadableException.class})
    protected ResponseEntity<ErrorInfo> handleBadRequest(HttpServletRequest req, Exception ex) {
        return new ResponseEntity<ErrorInfo>(new ErrorInfo(req.getRequestURI()+"?"+req.getQueryString(), ex), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(NoSuchElementException.class)
    protected ResponseEntity<ErrorInfo> handleNoEntryFound(HttpServletRequest req, Exception ex) {
        return new ResponseEntity<ErrorInfo>(new ErrorInfo(req.getRequestURI() + "?" + req.getQueryString(), ex, ex.getCause()), HttpStatus.NOT_FOUND);
    }


}
