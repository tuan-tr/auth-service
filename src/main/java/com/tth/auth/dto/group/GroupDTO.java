package com.tth.auth.dto.group;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tth.auth.entity.User;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(value = Include.NON_NULL)
public class GroupDTO {

  private String id;
  private String name;
  private Boolean enabled;
  private User createdBy;
  
}
