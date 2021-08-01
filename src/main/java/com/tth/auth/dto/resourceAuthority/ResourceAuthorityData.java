package com.tth.auth.dto.resourceAuthority;

import java.time.OffsetDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tth.auth.constant.ResourcePermission;
import com.tth.auth.constant.ResourceType;

public interface ResourceAuthorityData {
  
  ResourceType getTargetType();
  
  User getTargetUser();
  
  Group getTargetGroup();
  
  ResourceType getResourceType();

  User getResourceUser();
  
  Group getResourceGroup();
  
  OffsetDateTime getModifiedAt();
  
  User getModifiedBy();
  
  @JsonIgnore
  int getPermissions();

  @JsonProperty("permissions")
  default List<ResourcePermission> getPermissionList() {
    return ResourcePermission.revert(this.getPermissions());
  }
  
  interface User {
    String getId();
    Boolean getEnabled();
    PersonalInformation getPersonalInformation();
    
    interface PersonalInformation {
      String getFirstName();
      String getLastName();
    }
  }
  
  interface Group {
    String getId();
    String getName();
    Boolean getEnabled();
  }
  
}
