package com.WWI16AMA.backend_api.Member;

import com.WWI16AMA.backend_api.ErrorInfo;
import com.WWI16AMA.backend_api.Exception.EntryNotFoundException;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class MemberControllerAdvise {


    @ExceptionHandler
    protected ResponseEntity<ErrorInfo> handleBadRequest(HttpServletRequest req, Exception ex) {
        return new ResponseEntity<ErrorInfo>(new ErrorInfo(ex.getClass().toString(), ex), HttpStatus.BAD_REQUEST);
    }

    /*
    @ExceptionHandler(EntryNotFoundException.class)
    protected ResponseEntity<ErrorInfo> handleNoEntryFound(HttpServletRequest req, Exception ex) {
        return new ResponseEntity<ErrorInfo>(new ErrorInfo(req.getRequestURL().toString() + "?" + req.getQueryString(), ex), HttpStatus.NOT_FOUND);
    }
    */
}
