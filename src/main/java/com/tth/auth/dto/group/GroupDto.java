package com.tth.auth.dto.group;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tth.auth.dto.user.UserDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GroupDto {

  private String id;
  private String name;
  private Boolean enabled;
  private UserDto createdBy;
  private OffsetDateTime createdAt;
  
}
