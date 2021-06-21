package com.tth.auth.dto.authentication;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SigninRequest {
  
  @NotNull
  @Size(min = 1, max = 100)
  private String username;
  
  @NotNull
  @Size(min = 1, max = 72)
  private String password;
  
}
