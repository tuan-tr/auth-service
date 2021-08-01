package com.tth.auth.dto.group;

import java.time.OffsetDateTime;

public interface GroupMemberData {

  OffsetDateTime getModifiedAt();
  ModifiedBy getModifiedBy();
  User getUser();

  interface User {
    String getId();
    String getUsername();
    Boolean getEnabled();
    PersonalInformation getPersonalInformation();
    
    interface PersonalInformation {
      String getFirstName();
      String getLastName();
    }
  }
  
  interface ModifiedBy {
    String getId();
    PersonalInformation getPersonalInformation();
    
    interface PersonalInformation {
      String getFirstName();
      String getLastName();
    }
  }

}
