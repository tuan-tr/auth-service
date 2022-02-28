package com.tth.auth.dto.groupMember;

import javax.validation.constraints.Size;

import lombok.Value;

@Value
public class GroupMemberCriteria {

  @Size(min = 1, max = 100)
  private String keyword;

}
