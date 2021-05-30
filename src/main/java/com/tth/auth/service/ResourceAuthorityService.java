package com.tth.auth.service;

import java.util.List;

import com.tth.auth.dto.resourceAuthority.ResourceAccessCredential;
import com.tth.auth.dto.resourceAuthority.ResourcePermission;
import com.tth.auth.entity.ResourceAuthority;
import com.tth.auth.repository.ResourceAuthorityRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ResourceAuthorityService {

  @Autowired
  private ResourceAuthorityRepository resourceAuthorityRepository;

  public boolean hasPermission(ResourceAccessCredential credential) {
    Pageable limit1Filter = PageRequest.of(0, 1);

    int permissions = credential.getPermissions().stream()
        .mapToInt(ResourcePermission::getCode)
        .sum();

    List<ResourceAuthority> resourceAuthorities;
    boolean isVerifyOnResourceType = credential.getResourceId() == null;
    if (isVerifyOnResourceType) {
      resourceAuthorities = resourceAuthorityRepository.findMatchedOnResourceType(
          credential.getResourceType(),
          credential.getTargetIds(),
          permissions,
          limit1Filter);
    } else {
      resourceAuthorities = resourceAuthorityRepository.findMatchedOnSpecificResource(
          credential.getResourceType(),
          credential.getResourceId(),
          credential.getTargetIds(),
          permissions,
          limit1Filter);
    }

    boolean hasPermission = resourceAuthorities.size() > 0;
    return hasPermission;
  }

}
