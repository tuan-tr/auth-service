package com.tth.auth.exception;

import org.springframework.http.HttpStatus;

public abstract class CustomException extends RuntimeException {

  public abstract Object getDetail();
  public abstract HttpStatus getHttpStatus();

}
