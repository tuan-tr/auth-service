package com.tth.auth.context;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.tth.auth.configuration.setting.UserSetting;
import com.tth.auth.dto.resourceAuthority.ResourcePermission;
import com.tth.auth.dto.resourceAuthority.ResourceType;
import com.tth.auth.entity.ResourceAuthority;
import com.tth.auth.entity.User;
import com.tth.auth.repository.ResourceAuthorityRepository;
import com.tth.auth.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@Transactional
public class InitializationListener implements ApplicationListener<ApplicationReadyEvent> {

  @Autowired
  private UserSetting adminUserSetting;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private ResourceAuthorityRepository resourceAuthorityRepository;

  @Override
  public void onApplicationEvent(ApplicationReadyEvent event) {
    this.initAdminUser();
  }

  private void initAdminUser() {
    boolean adminUserExisted = userRepository.findByUsername(
        adminUserSetting.getUsername())
            .isPresent();

    if (adminUserExisted) {
      return;
    }

    User adminUser = User.builder()
        .username(adminUserSetting.getUsername())
        .password(passwordEncoder.encode(adminUserSetting.getPassword()))
        .enabled(true)
        .rootUser(true)
        .build();

    adminUser.setCreatedBy(adminUser);
    adminUser.setModifiedBy(adminUser);
    userRepository.save(adminUser);

    List<ResourceAuthority> resourceAuthorities = Stream.of(ResourceType.values())
        .map(resourceType -> ResourceAuthority.builder()
            .targetType(ResourceType.USER)
            .targetId(adminUser.getId())
            .resourceType(resourceType)
            .permissions(ResourcePermission.ALL.getCode())
            .modifiedBy(adminUser)
            .build())
        .collect(Collectors.toList());

    resourceAuthorityRepository.saveAll(resourceAuthorities);

    log.info("Initialized admin user with username: {}", adminUser.getUsername());
  }

}
