package com.tth.auth.dto.group;

import javax.validation.constraints.Size;

import lombok.Value;

@Value
public class GroupCriteria {

  private Boolean enabled;

  @Size(min=1, max=100)
  private String keyword;

}
