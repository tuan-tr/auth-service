package com.tth.auth.dto.resourceAuthority;

import java.util.Collection;
import java.util.UUID;

import com.tth.auth.constant.ResourcePermission;
import com.tth.auth.constant.ResourceType;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Singular;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ResourceAccessCredential {

  private ResourceType resourceType;
  private String resourceId;
  private Collection<UUID> targetIds;
  
  @Singular
  private Collection<ResourcePermission> permissions;

}
