package com.sparta.usinsa.presentation.common.exception;

public class InvalidRequestException extends CustomException {

  public InvalidRequestException(String message) {
    super(message, 400);
  }
}
