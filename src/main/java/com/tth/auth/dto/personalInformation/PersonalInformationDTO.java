package com.tth.auth.dto.personalInformation;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;

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
public class PersonalInformationDTO {
  
  private String id;
  
  private String firstName;
  
  private String lastName;
  
  private LocalDate birthdate;
  
  private Boolean gender;
  
}
