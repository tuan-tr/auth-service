package com.tth.auth.exception.responseMixIn;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class BindExceptionMixIn {
  
  @JsonIgnore abstract Object getArguments();
  @JsonIgnore abstract Object getCodes();
  
}
