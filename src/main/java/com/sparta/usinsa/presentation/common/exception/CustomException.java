package com.sparta.usinsa.presentation.common.exception;

import org.springframework.http.HttpStatus;

public class CustomException extends RuntimeException {

  private final String message;
  private final HttpStatus statusCode;


  public CustomException(String message, HttpStatus statusCode) {
    super(message);
    this.message = message;
    this.statusCode = statusCode;
  }

  public String getMessage() {
    return message;
  }

  public HttpStatus getStatusCode() {
    return statusCode;
  }

  public String getErrorCode() {
    return "ERROR_" + getStatusCode();
  }
}