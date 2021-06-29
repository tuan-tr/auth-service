package com.tth.auth.dto.group;

import java.time.OffsetDateTime;
import java.util.UUID;

public interface GroupMemberData {

  OffsetDateTime getModifiedAt();
  ModifiedBy getModifiedBy();
  User getUser();

  interface User {
    UUID getId();
    String getUsername();
    Boolean getEnabled();
    PersonalInformation getPersonalInformation();
    
    interface PersonalInformation {
      String getFirstName();
      String getLastName();
    }
  }
  
  interface ModifiedBy {
    UUID getId();
    PersonalInformation getPersonalInformation();
    
    interface PersonalInformation {
      String getFirstName();
      String getLastName();
    }
  }

}
