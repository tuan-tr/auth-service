package com.tth.auth.dto.resourceAuthority;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResourcePermission {

  ALL(Integer.MAX_VALUE),
  GRANT_PERMISSION(1),
  CREATE(2),
  READ(4),
  UPDATE(8),
  DELETE(16),
  ENABLE(32),
  ADD_ELEMENT(64);

  int code;
  
  public static List<ResourcePermission> revert(int permissions) {
    return Stream.of(ResourcePermission.values())
        .filter(value -> (permissions & value.getCode()) == value.getCode())
        .collect(Collectors.toList());
  }
  
  public static int sum(Collection<ResourcePermission> permissions) {
    return permissions.stream()
        .mapToInt(ResourcePermission::getCode)
        .sum();
  }
  
  public static int sum(ResourcePermission... permissions) {
    return Arrays.asList(permissions).stream()
        .mapToInt(ResourcePermission::getCode)
        .sum();
  }
  
  public static int add(int permission, Collection<ResourcePermission> newPermissions) {
    for (ResourcePermission newPermission : newPermissions) {
      if ((permission & newPermission.getCode()) == 0) {
        permission += newPermission.getCode();
      }
    }
    return permission;
  }
  
  public static int subtract(int permission, Collection<ResourcePermission> removePermissions) {
    for (ResourcePermission removePermission : removePermissions) {
      if ((permission & removePermission.getCode()) == removePermission.getCode()) {
        permission -= removePermission.getCode();
      }
    }
    return permission;
  }
  
}
