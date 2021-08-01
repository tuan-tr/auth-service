package com.tth.auth.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tth.auth.dto.personalInformation.PersonalInformationDTO;

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
public class UserDTO {
  
  private String id;
  
  private String username;
  
  private Boolean enabled;
  
  private PersonalInformationDTO personalInformation;

}
