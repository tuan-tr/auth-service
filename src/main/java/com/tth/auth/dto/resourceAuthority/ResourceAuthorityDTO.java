package com.tth.auth.dto.resourceAuthority;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tth.auth.constant.ResourcePermission;
import com.tth.auth.constant.ResourceType;

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
  
  private String id;
  
  private ResourceType targetType;
  
  private ResourceType resourceType;
  
  private List<ResourcePermission> permissions;
  
}
