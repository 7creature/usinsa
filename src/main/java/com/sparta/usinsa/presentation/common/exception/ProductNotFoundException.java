package com.sparta.usinsa.presentation.common.exception;

import org.springframework.http.HttpStatus;
public class ProductNotFoundException extends CustomException {

  public ProductNotFoundException(String message) {

    super(message, HttpStatus.NOT_FOUND);
  }
}