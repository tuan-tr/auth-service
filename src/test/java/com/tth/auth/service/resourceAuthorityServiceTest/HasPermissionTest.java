package com.tth.auth.service.resourceAuthorityServiceTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import com.tth.auth.constant.ResourcePermission;
import com.tth.auth.constant.ResourceType;
import com.tth.auth.dto.resourceAuthority.ResourceAccessCredential;
import com.tth.auth.entity.ResourceAuthority;
import com.tth.auth.repository.ResourceAuthorityRepository;
import com.tth.auth.service.ResourceAuthorityService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class HasPermissionTest {

  @TestConfiguration
  static class Configuration {
    @Bean
    ResourceAuthorityService resourceAuthorityService() {
      return new ResourceAuthorityService();
    }
  }

  @Autowired
  private ResourceAuthorityService resourceAuthorityService;

  @MockBean
  private ResourceAuthorityRepository resourceAuthorityRepository;

  @Test
  public void hasPermission_false_verifyOnSpecificResource() {
    ResourceType resourceType = ResourceType.USER;
    UUID resourceId = UUID.randomUUID();
    List<UUID> targetIds = Arrays.asList(UUID.randomUUID());
    List<ResourcePermission> permissions = Arrays.asList(ResourcePermission.READ);

    ResourceAccessCredential credential = ResourceAccessCredential.builder()
        .resourceType(resourceType)
        .resourceId(resourceId.toString())
        .targetIds(targetIds)
        .permissions(permissions)
        .build();

    when(resourceAuthorityRepository.findOnSpecificResource(
            any(ResourceType.class),
            anyString(),
            ArgumentMatchers.<UUID>anyList(),
            anyInt(),
            any(Pageable.class))
        ).thenReturn(Collections.emptyList());

    boolean hasPermission = resourceAuthorityService.hasPermission(credential);
    assertFalse(hasPermission);
  }

  @Test
  public void hasPermission_true_verifyOnSpecificResource() {
    ResourceType resourceType = ResourceType.USER;
    UUID resourceId = UUID.randomUUID();
    List<UUID> targetIds = Arrays.asList(UUID.randomUUID());
    List<ResourcePermission> permissions = Arrays.asList(ResourcePermission.READ);

    ResourceAccessCredential credential = ResourceAccessCredential.builder()
        .resourceType(resourceType)
        .resourceId(resourceId.toString())
        .targetIds(targetIds)
        .permissions(permissions)
        .build();

    when(resourceAuthorityRepository.findOnSpecificResource(
            any(ResourceType.class),
            anyString(),
            ArgumentMatchers.<UUID>anyList(),
            anyInt(),
            any(Pageable.class))
        ).thenReturn(Arrays.asList(new ResourceAuthority()));

    boolean hasPermission = resourceAuthorityService.hasPermission(credential);
    assertTrue(hasPermission);
  }

  @Test
  public void hasPermission_false_verifyOnResourceType() {
    ResourceType resourceType = ResourceType.USER;
    List<UUID> targetIds = Arrays.asList(UUID.randomUUID());
    List<ResourcePermission> permissions = Arrays.asList(ResourcePermission.READ);

    ResourceAccessCredential credential = ResourceAccessCredential.builder()
        .resourceType(resourceType)
        .targetIds(targetIds)
        .permissions(permissions)
        .build();

    when(resourceAuthorityRepository.findOnResourceType(
            any(ResourceType.class),
            ArgumentMatchers.<UUID>anyList(),
            anyInt(),
            any(Pageable.class))
        ).thenReturn(Collections.emptyList());

    boolean hasPermission = resourceAuthorityService.hasPermission(credential);
    assertFalse(hasPermission);
  }

  @Test
  public void hasPermission_true_verifyOnResourceType() {
    ResourceType resourceType = ResourceType.USER;
    List<UUID> targetIds = Arrays.asList(UUID.randomUUID());
    List<ResourcePermission> permissions = Arrays.asList(ResourcePermission.READ);

    ResourceAccessCredential credential = ResourceAccessCredential.builder()
        .resourceType(resourceType)
        .targetIds(targetIds)
        .permissions(permissions)
        .build();

    when(resourceAuthorityRepository.findOnResourceType(
            any(ResourceType.class),
            ArgumentMatchers.<UUID>anyList(),
            anyInt(),
            any(Pageable.class))
        ).thenReturn(Arrays.asList(new ResourceAuthority()));

    boolean hasPermission = resourceAuthorityService.hasPermission(credential);
    assertTrue(hasPermission);
  }

}
