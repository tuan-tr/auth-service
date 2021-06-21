package com.tth.auth.dto.user;

import java.time.LocalDate;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public interface UserInfor {
  
  UUID getId();
  Boolean getEnabled();
  String getUsername();
  PersonalInformation getPersonalInformation();
  
  interface PersonalInformation {
    String getFirstName();
    String getLastName();
    LocalDate getBirthdate();
    Boolean getGender();
  }
  
}
