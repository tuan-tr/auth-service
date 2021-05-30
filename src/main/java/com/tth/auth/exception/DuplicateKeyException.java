package com.tth.auth.exception;

import java.util.HashMap;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DuplicateKeyException extends CustomException {

  private String fieldName;
  private Object value;

  @Override
  public Object getDetail() {
    HashMap<String, Object> detail = new HashMap<>();
    detail.put("fieldName", this.fieldName);
    detail.put("value", this.value);
    return detail;
  }

  @Override
  public HttpStatus getHttpStatus() {
    return HttpStatus.BAD_REQUEST;
  }

}
