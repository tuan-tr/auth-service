package com.tth.auth.exception;

import java.util.Collection;

import com.tth.auth.constant.ResourcePermission;
import com.tth.auth.constant.ResourceType;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResourcePermissionNotFoundException extends CustomException {

  private ResourceType resourceType;
  private Object resourceId;
  private Collection<ResourcePermission> permissions;

  public ResourcePermissionNotFoundException(ResourceType resourceType, Collection<ResourcePermission> permissions) {
    this.resourceType = resourceType;
    this.permissions = permissions;
  }

  @Override
  public HttpStatus getHttpStatus() {
    return HttpStatus.FORBIDDEN;
  }

}
