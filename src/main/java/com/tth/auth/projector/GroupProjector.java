package com.tth.auth.projector;

import java.util.List;
import java.util.stream.Collectors;

import com.tth.auth.dto.group.GroupDto;
import com.tth.auth.dto.personalInformation.PersonalInformationDto;
import com.tth.auth.dto.user.UserDto;
import com.tth.auth.entity.Group;
import com.tth.auth.entity.PersonalInformation;
import com.tth.auth.entity.User;

public class GroupProjector {
  
  public static List<GroupDto> convertToDto(List<Group> entities) {
    return entities.stream()
        .map(entity -> convertToDto(entity))
        .collect(Collectors.toList());
  }
  
  public static GroupDto convertToDto(Group entity) {
    User creator = entity.getCreatedBy();
    PersonalInformation creatorInfor = creator.getPersonalInformation();
    return GroupDto.builder()
        .id(entity.getId())
        .name(entity.getName())
        .enabled(entity.isEnabled())
        .createdAt(entity.getCreatedAt())
        .createdBy(UserDto.builder()
            .id(creator.getId())
            .personalInformation(creatorInfor == null ? null : PersonalInformationDto.builder()
                .firstName(creatorInfor.getFirstName())
                .lastName(creatorInfor.getLastName())
                .build())
            .build())
        .build();
  }
  
}
