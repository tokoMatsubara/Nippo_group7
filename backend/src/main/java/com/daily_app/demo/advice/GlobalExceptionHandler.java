package com.daily_app.demo.advice;

import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleDuplicate(DataIntegrityViolationException e){
        System.err.println(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("status", "failed", "message", "日報データのアクセスに失敗しました"));
    }
}
