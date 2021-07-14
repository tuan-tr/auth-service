package com.tth.auth.dto.resourceAuthority;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

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
    UUID getId();
    Boolean getEnabled();
    PersonalInformation getPersonalInformation();
    
    interface PersonalInformation {
      String getFirstName();
      String getLastName();
    }
  }
  
  interface Group {
    UUID getId();
    String getName();
    Boolean getEnabled();
  }
  
}
