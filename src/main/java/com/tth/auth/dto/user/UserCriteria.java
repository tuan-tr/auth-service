package com.tth.auth.dto.user;

import javax.validation.constraints.Size;

import lombok.Value;

@Value
public class UserCriteria {
  
  @Size(min=1, max=100)
  private String keyword;
  
  private Boolean enabled;
  
  private Boolean gender;
  
}
