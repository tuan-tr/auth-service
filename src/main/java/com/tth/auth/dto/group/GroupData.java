package com.tth.auth.dto.group;

import java.time.OffsetDateTime;
import java.util.UUID;

public interface GroupData {

  UUID getId();
  String getName();
  Boolean getEnabled();
  OffsetDateTime getCreatedAt();
  User getCreatedBy();
  
  interface User {
    UUID getId();
    PersonalInformation getPersonalInformation();
    
    interface PersonalInformation {
      String getFirstName();
      String getLastName();
    }
  }
  
}
