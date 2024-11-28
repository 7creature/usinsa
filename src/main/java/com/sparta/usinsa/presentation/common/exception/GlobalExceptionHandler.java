package com.sparta.usinsa.presentation.common.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.HttpStatus;

@RestControllerAdvice
public class GlobalExceptionHandler {
  // CustomException을 처리하는 핸들러
  @ExceptionHandler(CustomException.class)
  public ResponseEntity<String> handleCustomException(CustomException ex) {
    return ResponseEntity.status(ex.getStatusCode())
        .body(ex.getMessage());
  }
  // ProductNotFoundException을 처리하는 핸들러
  @ExceptionHandler(ProductNotFoundException.class)
  public ResponseEntity<String> handleProductNotFoundException(ProductNotFoundException ex) {
    return ResponseEntity.status(ex.getStatusCode())
        .body(ex.getMessage());
  }
}