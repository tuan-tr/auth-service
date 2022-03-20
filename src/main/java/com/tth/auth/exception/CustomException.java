package com.tth.auth.exception;

import org.springframework.http.HttpStatus;

public abstract class CustomException extends RuntimeException {
  
  public abstract HttpStatus getHttpStatus();
  
  // public Object getDetail() {
  //   ObjectMapper objectMapper = new ObjectMapper();
  //   objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
  //   objectMapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);
  //   objectMapper.addMixIn(this.getClass(), CustomExceptionMixIn.class);
  //   Object rs = objectMapper.convertValue(this, Object.class);
  //   return rs;
  // }
  
}
