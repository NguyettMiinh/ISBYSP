package com.example.identity_final.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

//tat ca loi se dc xu li o day
@ControllerAdvice
public class GlobalExceptionHandler {

    //khai bao loai exception
    @ExceptionHandler(value = RuntimeException.class)
    ResponseEntity<String> handlingRuntimeException(RuntimeException exception) {
        //spring inject exception vao param de co the lay thong tin cua runtimeex
        //tra ve loi
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

}
