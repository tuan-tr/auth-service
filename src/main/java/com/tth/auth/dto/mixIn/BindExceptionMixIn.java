package com.tth.auth.dto.mixIn;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class BindExceptionMixIn {
  
  @JsonIgnore abstract Object getArguments();
  @JsonIgnore abstract Object getCodes();
  
}
