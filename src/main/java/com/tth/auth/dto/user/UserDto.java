package com.tth.auth.dto.user;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tth.auth.dto.personalInformation.PersonalInformationDto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
  
  private String id;
  private String username;
  private Boolean enabled;
  private PersonalInformationDto personalInformation;
  private UserDto createdBy;
  private OffsetDateTime createdAt;
  private UserDto modifiedBy;
  private OffsetDateTime modifiedAt;
  
}
