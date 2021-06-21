package com.tth.auth.dto.resourceAuthority;

import java.util.Arrays;
import java.util.Collection;

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

}
