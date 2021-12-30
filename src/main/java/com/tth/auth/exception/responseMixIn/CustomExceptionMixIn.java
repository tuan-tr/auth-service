package com.tth.auth.exception.responseMixIn;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class CustomExceptionMixIn {
  
  @JsonIgnore abstract Object getCause();
  @JsonIgnore abstract Object getStackTrace();
  @JsonIgnore abstract Object getSuppressed();
  @JsonIgnore abstract Object getMessage();
  @JsonIgnore abstract Object getLocalizedMessage();
  @JsonIgnore abstract Object getHttpStatus();
  @JsonIgnore abstract Object getDetail();
  
}
