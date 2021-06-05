package com.tth.auth.dto.authentication;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SignupRequest {

  @NotBlank
  @Size(min = 1, max = 100)
  private String username;

  @NotBlank
  @Size(min = 1, max = 72)
  private String password;

}
