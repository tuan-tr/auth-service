package com.tth.auth.service.resourceAuthorityServiceTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.List;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.tth.auth.constant.ResourcePermission;
import com.tth.auth.constant.ResourceType;
import com.tth.auth.dto.resourceAuthority.ResourceAccessCredential;
import com.tth.auth.repository.ResourceAuthorityRepository;
import com.tth.auth.service.GroupService;
import com.tth.auth.service.ResourceAuthorityService;
import com.tth.auth.service.UserService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class HasPermissionTest {

  @TestConfiguration
  static class Configuration {
    @Bean
    ResourceAuthorityService resourceAuthorityService() {
      return new ResourceAuthorityService();
    }
    
    @MockBean
    private UserService userService;
    
    @MockBean
    private GroupService groupService;
  }

  @Autowired
  private ResourceAuthorityService resourceAuthorityService;

  @MockBean
  private ResourceAuthorityRepository resourceAuthorityRepository;

  @Test
  public void hasPermission_false_verifyOnSpecificResource() {
    ResourceType resourceType = ResourceType.USER;
    String resourceId = NanoIdUtils.randomNanoId();
    List<String> targetIds = List.of(NanoIdUtils.randomNanoId());
    List<ResourcePermission> permissions = List.of(ResourcePermission.READ);

    ResourceAccessCredential credential = ResourceAccessCredential.builder()
        .resourceType(resourceType)
        .resourceId(resourceId)
        .targetIds(targetIds)
        .permissions(permissions)
        .build();

    when(resourceAuthorityRepository.findFirstIdMatchToSpecificResource(
            resourceType.toString(),
            resourceId,
            targetIds,
            ResourcePermission.sum(permissions))
        ).thenReturn(null);

    boolean hasPermission = resourceAuthorityService.hasPermission(credential);
    assertFalse(hasPermission);
  }

  @Test
  public void hasPermission_true_verifyOnSpecificResource() {
    ResourceType resourceType = ResourceType.USER;
    String resourceId = NanoIdUtils.randomNanoId();
    List<String> targetIds = List.of(NanoIdUtils.randomNanoId());
    List<ResourcePermission> permissions = List.of(ResourcePermission.READ);

    ResourceAccessCredential credential = ResourceAccessCredential.builder()
        .resourceType(resourceType)
        .resourceId(resourceId)
        .targetIds(targetIds)
        .permissions(permissions)
        .build();

    when(resourceAuthorityRepository.findFirstIdMatchToSpecificResource(
            resourceType.toString(),
            resourceId,
            targetIds,
            ResourcePermission.sum(permissions))
        ).thenReturn("id");

    boolean hasPermission = resourceAuthorityService.hasPermission(credential);
    assertTrue(hasPermission);
  }

  @Test
  public void hasPermission_false_verifyOnResourceType() {
    ResourceType resourceType = ResourceType.USER;
    List<String> targetIds = List.of(NanoIdUtils.randomNanoId());
    List<ResourcePermission> permissions = List.of(ResourcePermission.READ);

    ResourceAccessCredential credential = ResourceAccessCredential.builder()
        .resourceType(resourceType)
        .targetIds(targetIds)
        .permissions(permissions)
        .build();

    when(resourceAuthorityRepository.findFirstIdMatchToResourceType(
            resourceType.toString(),
            targetIds,
            ResourcePermission.sum(permissions))
        ).thenReturn(null);

    boolean hasPermission = resourceAuthorityService.hasPermission(credential);
    assertFalse(hasPermission);
  }

  @Test
  public void hasPermission_true_verifyOnResourceType() {
    ResourceType resourceType = ResourceType.USER;
    List<String> targetIds = List.of(NanoIdUtils.randomNanoId());
    List<ResourcePermission> permissions = List.of(ResourcePermission.READ);

    ResourceAccessCredential credential = ResourceAccessCredential.builder()
        .resourceType(resourceType)
        .targetIds(targetIds)
        .permissions(permissions)
        .build();

    when(resourceAuthorityRepository.findFirstIdMatchToResourceType(
            resourceType.toString(),
            targetIds,
            ResourcePermission.sum(permissions))
        ).thenReturn("id");

    boolean hasPermission = resourceAuthorityService.hasPermission(credential);
    assertTrue(hasPermission);
  }

}
