package com.tth.auth.dto.user;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.tth.auth.dto.personalInformation.PersonalInformationInput;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserInput {

  @NotBlank
  @Size(min = 1, max=100)
  private String username;

  private boolean enabled;

  @NotNull
  private PersonalInformationInput personalInformation;

}
