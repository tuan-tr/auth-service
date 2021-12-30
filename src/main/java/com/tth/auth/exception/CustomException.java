package com.tth.auth.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.tth.auth.exception.responseMixIn.CustomExceptionMixIn;

import org.springframework.http.HttpStatus;

public abstract class CustomException extends RuntimeException {
  
  public abstract HttpStatus getHttpStatus();
  
  public Object getDetail() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    objectMapper.addMixIn(this.getClass(), CustomExceptionMixIn.class);
    Object rs = objectMapper.convertValue(this, Object.class);
    return rs;
  }
  
}
