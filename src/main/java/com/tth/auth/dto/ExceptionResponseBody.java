package com.tth.auth.dto;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ExceptionResponseBody {

  private OffsetDateTime timestamp;
  private String path;
  private int status;
  private String error;
  private String message;
  private Object detail;

  public String getError() {
    return this.error.replace("Exception", "");
  }

}
