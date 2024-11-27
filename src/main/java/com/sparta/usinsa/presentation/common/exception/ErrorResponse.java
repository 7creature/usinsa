package com.sparta.usinsa.presentation.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {
  private String errorCode;
  private String message;
}