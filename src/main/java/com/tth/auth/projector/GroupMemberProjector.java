package com.tth.auth.projector;

import java.util.List;
import java.util.stream.Collectors;

import com.tth.auth.dto.groupMember.GroupMemberDto;
import com.tth.auth.dto.personalInformation.PersonalInformationDto;
import com.tth.auth.dto.user.UserDto;
import com.tth.auth.entity.GroupMember;
import com.tth.auth.entity.PersonalInformation;
import com.tth.auth.entity.User;

public class GroupMemberProjector {
  
  public static List<GroupMemberDto> convertToDto(List<GroupMember> entities) {
    return entities.stream()
        .map(entity -> convertToDto(entity))
        .collect(Collectors.toList());
  }
  
  public static GroupMemberDto convertToDto(GroupMember entity) {
    User user = entity.getUser();
    PersonalInformation userInfor = user.getPersonalInformation();
    User modifier = entity.getModifiedBy();
    PersonalInformation modifierInfor = modifier.getPersonalInformation();
    return GroupMemberDto.builder()
        .user(UserDto.builder()
            .id(user.getId())
            .username(user.getUsername())
            .enabled(user.isEnabled())
            .personalInformation(userInfor == null ? null : PersonalInformationDto.builder()
                .firstName(userInfor.getFirstName())
                .lastName(userInfor.getLastName())
                .build())
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
