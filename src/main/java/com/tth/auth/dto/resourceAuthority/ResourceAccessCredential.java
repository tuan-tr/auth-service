package com.tth.auth.dto.resourceAuthority;

import java.util.Collection;
import java.util.UUID;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ResourceAccessCredential {

  private ResourceType resourceType;
  private String resourceId;
  private Collection<UUID> targetIds;
  private Collection<ResourcePermission> permissions;

}
