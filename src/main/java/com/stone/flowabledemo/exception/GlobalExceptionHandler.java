package com.stone.flowabledemo.exception;

import com.stone.flowabledemo.constant.ResponseData;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author Administrator
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CheckException.class)
    public ResponseData<Object> checkExceptionHandler(Exception e){
        return new ResponseData<>().setMessage(e.getMessage()).failed();
    }


    @ExceptionHandler(Exception.class)
    public ResponseData<Object> exceptionHandler(Exception e){
        return new ResponseData<>().setMessage(e.getMessage()).error();
    }

}
