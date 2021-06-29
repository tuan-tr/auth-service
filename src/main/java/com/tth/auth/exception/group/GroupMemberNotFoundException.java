package com.tth.auth.exception.group;

import java.util.HashMap;
import java.util.UUID;

import com.tth.auth.exception.CustomException;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GroupMemberNotFoundException extends CustomException {

  private UUID groupId;
  private UUID userId;

  @Override
  public Object getDetail() {
    HashMap<String, Object> description = new HashMap<>();
    description.put("groupId", this.groupId);
    description.put("userId", this.userId);
    return description;
  }

  @Override
  public HttpStatus getHttpStatus() {
    return HttpStatus.NOT_FOUND;
  }

}
