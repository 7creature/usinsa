package com.sparta.usinsa.presentation.common.exception;

import org.springframework.http.HttpStatus;

public class InvalidRequestException extends CustomException {

  public InvalidRequestException(String message) {
    super(message, HttpStatus.BAD_REQUEST);
  }
}
