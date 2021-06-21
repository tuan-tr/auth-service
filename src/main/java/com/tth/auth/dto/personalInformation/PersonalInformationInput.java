package com.tth.auth.dto.personalInformation;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PersonalInformationInput {
  
  @NotBlank
  @Size(min = 1, max=100)
  private String firstName;
  
  @NotBlank
  @Size(min = 1, max=100)
  private String lastName;
  
  private LocalDate birthdate;
  
  private Boolean gender;
  
}
