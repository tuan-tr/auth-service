package com.tth.auth.exception;

import java.util.HashMap;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EntityNotFoundException extends CustomException {

  private String type;
  private Object id;

  public EntityNotFoundException(String type) {
    this.type = type;
  }

  @Override
  public Object getDetail() {
    HashMap<String, Object> detail = new HashMap<>();
    detail.put("type", this.type);
    detail.put("id", this.id);
    return detail;
  }

  @Override
  public HttpStatus getHttpStatus() {
    return HttpStatus.NOT_FOUND;
  }

}
