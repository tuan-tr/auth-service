package com.tth.auth.dto.resourceAuthority;

import java.util.List;
import java.util.UUID;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ResourceAuthorityInput {
  
  @NotNull
  private ResourceType targetType;
  
  @NotNull
  private UUID targetId;
  
  @NotNull
  private ResourceType resourceType;
  
  private String resourceId;
  
  @NotEmpty
  private List<ResourcePermission> permissions;
  
  @AssertTrue(message="targetType must be 'GROUP' or 'USER'")
  boolean isValidTargetType() {
    return targetType == null ^
        (targetType == ResourceType.GROUP || targetType == ResourceType.USER);
  }
  
}
