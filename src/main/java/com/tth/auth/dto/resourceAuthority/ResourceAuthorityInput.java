package com.tth.auth.dto.resourceAuthority;

import java.util.List;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.tth.auth.constant.ResourcePermission;
import com.tth.auth.constant.ResourceType;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ResourceAuthorityInput {
  
  @NotNull
  private ResourceType targetType;
  
  @NotBlank
  private String targetId;
  
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
  
  @AssertTrue(message="resourceId can be null or must not be blank")
  boolean isValidResourceId() {
    return resourceId == null || resourceId.trim().length() > 0;
  }
  
}
