package com.tth.auth.dto.resourceAuthority;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = Include.NON_NULL)
public class ResourceAuthorityDTO {
  
  private UUID id;
  
  private ResourceType targetType;
  
  private ResourceType resourceType;
  
  private List<ResourcePermission> permissions;
  
}
