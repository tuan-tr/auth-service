package com.tth.auth.dto.resourceAuthority;

import java.time.OffsetDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tth.auth.constant.ResourcePermission;
import com.tth.auth.constant.ResourceType;
import com.tth.auth.dto.user.UserDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = Include.NON_NULL)
public class ResourceAuthorityDto {
  
  private ResourceType targetType;
  
  private Object target;
  
  private ResourceType resourceType;
  
  private Object resource;
  
  private List<ResourcePermission> permissions;
  
  private OffsetDateTime modifiedAt;
  
  private UserDto modifiedBy;
  
}
