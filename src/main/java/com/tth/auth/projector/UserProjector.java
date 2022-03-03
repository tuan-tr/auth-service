package com.tth.auth.projector;

import java.util.List;
import java.util.stream.Collectors;

import com.tth.auth.dto.personalInformation.PersonalInformationDto;
import com.tth.auth.dto.user.UserDto;
import com.tth.auth.entity.PersonalInformation;
import com.tth.auth.entity.User;

public class UserProjector {
  
  public static List<UserDto> convertToBaseDto(List<User> entities) {
    return entities.stream()
        .map(entity -> convertToBaseDto(entity))
        .collect(Collectors.toList());
  }
  
  public static UserDto convertToBaseDto(User entity) {
    PersonalInformation userInfor = entity.getPersonalInformation();
    return UserDto.builder()
        .id(entity.getId())
        .username(entity.getUsername())
        .enabled(entity.isEnabled())
        .personalInformation(userInfor == null ? null : PersonalInformationDto.builder()
            .firstName(userInfor.getFirstName())
            .lastName(userInfor.getLastName())
            .build())
        .modifiedAt(entity.getModifiedAt())
        .build();
  }
  
  public static UserDto convertToDetailDto(User entity) {
    PersonalInformation userInfor = entity.getPersonalInformation();
    User modifier = entity.getModifiedBy();
    PersonalInformation modifierInfor = modifier.getPersonalInformation();
    return UserDto.builder()
        .id(entity.getId())
        .username(entity.getUsername())
        .enabled(entity.isEnabled())
        .personalInformation(userInfor == null ? null : PersonalInformationDto.builder()
            .firstName(userInfor.getFirstName())
            .lastName(userInfor.getLastName())
            .birthdate(userInfor.getBirthdate())
            .gender(userInfor.getGender())
            .build())
        .modifiedAt(entity.getModifiedAt())
        .modifiedBy(UserDto.builder()
            .id(modifier.getId())
            .personalInformation(modifierInfor == null ? null : PersonalInformationDto.builder()
                .firstName(modifierInfor.getFirstName())
                .lastName(modifierInfor.getLastName())
                .build())
            .build())
        .build();
  }
  
}
