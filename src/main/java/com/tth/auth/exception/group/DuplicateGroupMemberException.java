package com.tth.auth.exception.group;

import com.tth.auth.exception.CustomException;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DuplicateGroupMemberException extends CustomException {

  private String groupId;
  private String userId;

  @Override
  public HttpStatus getHttpStatus() {
    return HttpStatus.BAD_REQUEST;
  }

}
