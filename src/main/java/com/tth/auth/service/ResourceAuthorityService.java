package com.tth.auth.service;

import java.util.Collections;
import java.util.List;

import com.tth.auth.configuration.security.user.UserAuthority;
import com.tth.auth.constant.ResourcePermission;
import com.tth.auth.constant.ResourceType;
import com.tth.auth.dto.resourceAuthority.ResourceAccessCredential;
import com.tth.auth.dto.resourceAuthority.ResourceAuthorityCriteria;
import com.tth.auth.dto.resourceAuthority.ResourceAuthorityDto;
import com.tth.auth.dto.resourceAuthority.ResourceAuthorityInput;
import com.tth.auth.entity.ResourceAuthority;
import com.tth.auth.exception.EntityNotFoundException;
import com.tth.auth.exception.ResourcePermissionNotFoundException;
import com.tth.auth.projector.ResourceAuthorityProjector;
import com.tth.auth.repository.ResourceAuthorityRepository;
import com.tth.auth.util.CurrentUserContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ResourceAuthorityService {

  @Autowired
  private ResourceAuthorityRepository resourceAuthorityRepository;
  
  @Autowired
  private UserService userService;
  
  @Autowired
  private GroupService groupService;
  
  public void authenticate(ResourceAccessCredential credential) {
    boolean hasPermission = this.hasPermission(credential);
    if (hasPermission == false) {
      throw new ResourcePermissionNotFoundException(credential.getResourceType(),
          credential.getResourceId(), credential.getPermissions());
    }
  }
  
  public boolean hasPermission(ResourceAccessCredential credential) {
    int permissions = ResourcePermission.sum(credential.getPermissions());
    
    String resourceAuthorityId;
    boolean isVerifyOnResourceType = credential.getResourceId() == null;
    if (isVerifyOnResourceType) {
      resourceAuthorityId = resourceAuthorityRepository.findFirstIdMatchToResourceType(
          credential.getResourceType().toString(),
          credential.getTargetIds(),
          permissions);
    } else {
      resourceAuthorityId = resourceAuthorityRepository.findFirstIdMatchToSpecificResource(
          credential.getResourceType().toString(),
          credential.getResourceId(),
          credential.getTargetIds(),
          permissions);
    }
    
    boolean hasPermission = resourceAuthorityId != null;
    return hasPermission;
  }
  
  public List<String> getAuthorizedResourceIds(ResourceAccessCredential credential) {
    int permissions = ResourcePermission.sum(credential.getPermissions());
    List<String> resourceIds = resourceAuthorityRepository.findResourceIdsMatchToTargets(
          credential.getResourceType().toString(), 
          credential.getTargetIds(), 
          permissions);
    return resourceIds;
  }
  
  @Transactional
  public void grant(ResourceAuthorityInput input) {
    ResourceType targetType = input.getTargetType();
    String targetId = input.getTargetId();
    ResourceType resourceType = input.getResourceType();
    String resourceId = input.getResourceId();
    
    if (targetType == ResourceType.USER) {
      userService.getById(targetId);
    } else if (targetType == ResourceType.GROUP) {
      groupService.getById(targetId);
    }
    
    ResourceAuthority resourceAuthority = resourceAuthorityRepository
        .findOne(targetType, targetId, resourceType, resourceId)
        .orElse(ResourceAuthority.builder()
            .targetType(targetType)
            .targetId(targetId)
            .resourceType(resourceType)
            .resourceId(resourceId)
            .permissions(0)
            .build());
    
    int permissions = resourceAuthority.getPermissions();
    permissions = ResourcePermission.add(permissions, input.getPermissions());
    resourceAuthority.setPermissions(permissions);
    
    resourceAuthorityRepository.save(resourceAuthority);
  }
  
  @Transactional
  public void remove(ResourceAuthorityInput input) {
    ResourceType targetType = input.getTargetType();
    String targetId = input.getTargetId();
    ResourceType resourceType = input.getResourceType();
    String resourceId = input.getResourceId();
    
    if (targetType == ResourceType.USER) {
      userService.getById(targetId);
    } else if (targetType == ResourceType.GROUP) {
      groupService.getById(targetId);
    }
    
    ResourceAuthority resourceAuthority = resourceAuthorityRepository
        .findOne(targetType, targetId, resourceType, resourceId)
        .orElseThrow(() -> new EntityNotFoundException(ResourceAuthority.class.getSimpleName()));
    
    int permissions = ResourcePermission.subtract(
        resourceAuthority.getPermissions(), input.getPermissions());
    if (permissions == 0 ) {
      resourceAuthorityRepository.delete(resourceAuthority);
    } else {
      resourceAuthority.setPermissions(permissions);
    }
  }
  
  public Page<ResourceAuthorityDto> getList(ResourceAuthorityCriteria criteria, Pageable pageable) {
    UserAuthority currentUser = CurrentUserContext.get();
    
    Page<String> grantedIdPage = resourceAuthorityRepository
        .findIdsMatchGrantPermission(currentUser.getResourceAuthorities(), criteria, pageable);
    
    if (grantedIdPage.getTotalElements() == 0l) {
      return new PageImpl<>(Collections.emptyList(), grantedIdPage.getPageable(), 0);
    }
    
    List<ResourceAuthority> entities = resourceAuthorityRepository.findByIds(grantedIdPage.getContent());
    List<ResourceAuthorityDto> content = ResourceAuthorityProjector.convertToDto(entities);
    return new PageImpl<>(content, grantedIdPage.getPageable(), grantedIdPage.getTotalElements());
  }
  
}
