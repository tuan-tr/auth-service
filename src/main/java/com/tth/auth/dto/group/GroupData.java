package com.tth.auth.dto.group;

import java.time.OffsetDateTime;

public interface GroupData {

  String getId();
  String getName();
  Boolean getEnabled();
  OffsetDateTime getCreatedAt();
  User getCreatedBy();
  
  interface User {
    String getId();
    PersonalInformation getPersonalInformation();
    
    interface PersonalInformation {
      String getFirstName();
      String getLastName();
    }
  }
  
}
