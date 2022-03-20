package com.tth.auth.dto.mixIn;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class PageMixIn {
  
  @JsonIgnore abstract Object getPageable();
  
}
