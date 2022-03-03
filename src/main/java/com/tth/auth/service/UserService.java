package com.tth.auth.service;

import java.util.ArrayList;
import java.util.List;

import com.tth.auth.configuration.security.user.UserAuthority;
import com.tth.auth.constant.ResourcePermission;
import com.tth.auth.constant.ResourceType;
import com.tth.auth.dto.personalInformation.PersonalInformationInput;
import com.tth.auth.dto.resourceAuthority.ResourceAccessCredential;
import com.tth.auth.dto.user.UserCriteria;
import com.tth.auth.dto.user.UserDto;
import com.tth.auth.dto.user.UserInput;
import com.tth.auth.entity.PersonalInformation;
import com.tth.auth.entity.ResourceAuthority;
import com.tth.auth.entity.User;
import com.tth.auth.exception.DuplicateKeyException;
import com.tth.auth.exception.EntityNotFoundException;
import com.tth.auth.projector.UserProjector;
import com.tth.auth.repository.GroupMemberRepository;
import com.tth.auth.repository.ResourceAuthorityRepository;
import com.tth.auth.repository.UserRepository;
import com.tth.auth.util.CurrentUserContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
@Transactional(readOnly = true)
public class UserService implements UserDetailsService {
  
  @Autowired
  private UserRepository userRepository;
  
  @Autowired
  private GroupMemberRepository groupMemberRepository;
  
  @Autowired
  private PasswordEncoder passwordEncoder;
  
  @Autowired
  private String defaultUserPassword;
  
  @Autowired
  private ResourceAuthorityRepository resourceAuthorityRepository;
  
  @Autowired
  private ResourceAuthorityService resourceAuthorityService;
  
  @Override
  public UserDetails loadUserByUsername(String username) {
    User user = userRepository.findByUsername(username)
        .orElseThrow(()-> new UsernameNotFoundException(username));

    List<String> groupIds = groupMemberRepository.findEnabledGroupIds(user.getId());
    List<String> resourceAuthorities = new ArrayList<>(groupIds);
    resourceAuthorities.add(user.getId());

    return UserAuthority.builder()
        .id(user.getId())
        .resourceAuthorities(resourceAuthorities)
        .userEntity(user)
        .username(username)
        .password(user.getPassword())
        .accountNonExpired(true)
        .accountNonLocked(true)
        .credentialsNonExpired(true)
        .enabled(user.isEnabled())
        .build();
  }
  
  public User getById(String id) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException(
            User.class.getSimpleName(), id));
    return user;
  }
  
  @Transactional
  public UserDto create(UserInput input) {
    userRepository.findByUsername(input.getUsername())
        .ifPresent(user -> {
          throw new DuplicateKeyException("username", input.getUsername());
        });
    
    PersonalInformationInput informationInput = input.getPersonalInformation();
    User newUser = User.builder()
        .username(input.getUsername())
        .password(passwordEncoder.encode(defaultUserPassword))
        .enabled(input.isEnabled())
        .personalInformation(PersonalInformation.builder()
            .firstName(informationInput.getFirstName())
            .lastName(informationInput.getLastName())
            .birthdate(informationInput.getBirthdate())
            .gender(informationInput.getGender())
            .build())
        .build();
    userRepository.save(newUser);
    
    ResourceAuthority newUserAuthority = ResourceAuthority.builder()
        .targetType(ResourceType.USER)
        .targetId(newUser.getId())
        .resourceType(ResourceType.USER)
        .resourceId(newUser.getId())
        .permissions(ResourcePermission.sum(ResourcePermission.READ,
            ResourcePermission.UPDATE))
        .build();
    resourceAuthorityRepository.save(newUserAuthority);
    
    UserAuthority currentUser = CurrentUserContext.get();
    ResourceAuthority currentUserAuthorityOnNewUser = ResourceAuthority.builder()
        .targetType(ResourceType.USER)
        .targetId(currentUser.getId())
        .resourceType(ResourceType.USER)
        .resourceId(newUser.getId())
        .permissions(ResourcePermission.sum(ResourcePermission.READ,
            ResourcePermission.UPDATE,
            ResourcePermission.DELETE,
            ResourcePermission.ENABLE))
        .build();
    resourceAuthorityRepository.save(currentUserAuthorityOnNewUser);
    
    return UserDto.builder()
        .id(newUser.getId())
        .build();
  }
  
  public UserDto getInforById(String id) {
    User userInfor = userRepository.findInforById(id)
        .orElseThrow(() -> new EntityNotFoundException(
            User.class.getSimpleName(), id));
    
    UserDto dto = UserProjector.convertToDetailDto(userInfor);
    return dto;
  }
  
  public Page<UserDto> getList(UserCriteria criteria, Pageable pageable) {
    UserAuthority currentUser = CurrentUserContext.get();
    
    ResourceAccessCredential readCredential = ResourceAccessCredential.builder()
        .resourceType(ResourceType.USER)
        .targetIds(currentUser.getResourceAuthorities())
        .permission(ResourcePermission.READ)
        .build();
    
    boolean hasReadPermissionOnAllUser = resourceAuthorityService
        .hasPermission(readCredential);
    
    List<String> readableUserIds = null;
    if (hasReadPermissionOnAllUser == false) {
      readableUserIds = resourceAuthorityService.getAuthorizedResourceIds(readCredential);
      if (CollectionUtils.isEmpty(readableUserIds)) {
        return Page.empty();
      }
    }
    
    Page<User> page = userRepository.findList(readableUserIds, criteria, pageable);
    List<UserDto> content = UserProjector.convertToBaseDto(page.getContent());
    return new PageImpl<>(content, pageable, page.getTotalElements());
  }
  
  @Transactional
  public void enable(String id, boolean enabled) {
    User user = this.getById(id);
    user.setEnabled(enabled);
  }
  
  @Transactional
  public void updateInformation(String id, PersonalInformationInput input) {
    User user = this.getById(id);
    PersonalInformation information = user.getPersonalInformation();
    information.setFirstName(input.getFirstName());
    information.setLastName(input.getLastName());
    information.setBirthdate(input.getBirthdate());
    information.setGender(input.getGender());
  }
  
}
