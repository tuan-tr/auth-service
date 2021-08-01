package com.tth.auth.service;

import java.util.List;

import com.tth.auth.configuration.security.user.UserAuthority;
import com.tth.auth.constant.ResourcePermission;
import com.tth.auth.constant.ResourceType;
import com.tth.auth.dto.resourceAuthority.ResourceAccessCredential;
import com.tth.auth.dto.resourceAuthority.ResourceAuthorityCriteria;
import com.tth.auth.dto.resourceAuthority.ResourceAuthorityData;
import com.tth.auth.dto.resourceAuthority.ResourceAuthorityInput;
import com.tth.auth.entity.ResourceAuthority;
import com.tth.auth.exception.EntityNotFoundException;
import com.tth.auth.exception.ResourcePermissionNotFoundException;
import com.tth.auth.repository.ResourceAuthorityRepository;
import com.tth.auth.util.CurrentUserContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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
    Pageable limit1Filter = PageRequest.of(0, 1);

    int permissions = ResourcePermission.sum(credential.getPermissions());

    List<ResourceAuthority> resourceAuthorities;
    boolean isVerifyOnResourceType = credential.getResourceId() == null;
    if (isVerifyOnResourceType) {
      resourceAuthorities = resourceAuthorityRepository.findOnResourceType(
          credential.getResourceType(),
          credential.getTargetIds(),
          permissions,
          limit1Filter);
    } else {
      resourceAuthorities = resourceAuthorityRepository.findOnSpecificResource(
          credential.getResourceType(),
          credential.getResourceId(),
          credential.getTargetIds(),
          permissions,
          limit1Filter);
    }

    boolean hasPermission = resourceAuthorities.size() > 0;
    return hasPermission;
  }
  
  public List<String> getAuthorizedResourceIds(ResourceAccessCredential credential) {
    int permissions = ResourcePermission.sum(credential.getPermissions());
    List<String> resourceIds = resourceAuthorityRepository.findResourceIdsByTargets(
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
  
  public List<ResourceAuthorityData> getList(ResourceAuthorityCriteria criteria, Sort sort) {
    UserAuthority currentUser = CurrentUserContext.get();
    Integer permissions = null;
    if (CollectionUtils.isEmpty(criteria.getPermissions()) == false) {
      permissions = ResourcePermission.sum(criteria.getPermissions());
    }
    
    List<String> grantedIds = resourceAuthorityRepository.findIdsHaveGrantPermission(currentUser.getResourceAuthorities(),
        criteria.getTargetType(), criteria.getTargetId(),
        criteria.getResourceType(), criteria.getResourceId(),
        permissions, sort);
    
    List<ResourceAuthorityData> page = resourceAuthorityRepository.findList(grantedIds,
        criteria.getTargetType(), 
        criteria.getTargetId(),
        criteria.getResourceType(), criteria.getResourceId(),
        permissions, sort);
    
    return page;
  }
  
}
