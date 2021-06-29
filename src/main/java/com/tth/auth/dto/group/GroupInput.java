package com.tth.auth.dto.group;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GroupInput {
  
  @NotBlank
  @Size(max=100)
  private String name;
  
  private boolean enabled;
  
}
