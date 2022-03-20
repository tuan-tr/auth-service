package com.tth.auth.projector;

import java.util.List;
import java.util.stream.Collectors;

import com.tth.auth.constant.ResourcePermission;
import com.tth.auth.constant.ResourceType;
import com.tth.auth.dto.group.GroupDto;
import com.tth.auth.dto.resourceAuthority.ResourceAuthorityDto;
import com.tth.auth.dto.user.UserDto;
import com.tth.auth.entity.Group;
import com.tth.auth.entity.ResourceAuthority;
import com.tth.auth.entity.User;

public class ResourceAuthorityProjector {
  
  public static List<ResourceAuthorityDto> convertToDto(List<ResourceAuthority> entities) {
    return entities.stream()
        .map(entity -> convertToDto(entity))
        .collect(Collectors.toList());
  }
  
  public static ResourceAuthorityDto convertToDto(ResourceAuthority entity) {
    Object target = null;
    if (entity.getTargetType() == ResourceType.USER) {
      target = entity.getTargetId() == null ? null : convertUserToDto(entity.getTargetUser());
    } else if (entity.getTargetType() == ResourceType.GROUP) {
      target = entity.getTargetId() == null ? null : convertGroupToDto(entity.getTargetGroup());
    }
    
    Object resource = null;
    if (entity.getResourceType() == ResourceType.USER) {
      resource = entity.getResourceId() == null ? null : convertUserToDto(entity.getResourceUser());
    } else if (entity.getResourceType() == ResourceType.GROUP) {
      resource = entity.getResourceId() == null ? null : convertGroupToDto(entity.getResourceGroup());
    }
    
    User modifier = entity.getModifiedBy();
    
    return ResourceAuthorityDto.builder()
        .targetType(entity.getTargetType())
        .target(target)
        .resourceType(entity.getResourceType())
        .resource(resource)
        .permissions(ResourcePermission.revert(entity.getPermissions()))
        .modifiedAt(entity.getModifiedAt())
        .modifiedBy(UserDto.builder()
            .id(modifier.getId())
            .username(modifier.getUsername())
            .build())
        .build();
  }
  
  private static UserDto convertUserToDto(User entity) {
    return UserDto.builder()
        .id(entity.getId())
        .username(entity.getUsername())
        .enabled(entity.isEnabled())
        .build();
  }
  
  private static GroupDto convertGroupToDto(Group entity) {
    return GroupDto.builder()
        .id(entity.getId())
        .name(entity.getName())
        .enabled(entity.isEnabled())
        .build();
  }
  
}
