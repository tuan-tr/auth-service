package com.tth.auth.exception;

import java.util.Collection;
import java.util.HashMap;

import com.tth.auth.constant.ResourcePermission;
import com.tth.auth.constant.ResourceType;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;

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
  public Object getDetail() {
    HashMap<String, Object> detail = new HashMap<>();
    detail.put("resourceType", this.resourceType);
    detail.put("resourceId", this.resourceId);
    detail.put("permissions", this.permissions);
    return detail;
  }

  @Override
  public HttpStatus getHttpStatus() {
    return HttpStatus.FORBIDDEN;
  }

}
