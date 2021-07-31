package com.tth.auth.controller;

import java.util.List;

import javax.validation.Valid;

import com.tth.auth.configuration.security.user.UserAuthority;
import com.tth.auth.constant.ResourcePermission;
import com.tth.auth.dto.resourceAuthority.ResourceAccessCredential;
import com.tth.auth.dto.resourceAuthority.ResourceAuthorityCriteria;
import com.tth.auth.dto.resourceAuthority.ResourceAuthorityData;
import com.tth.auth.dto.resourceAuthority.ResourceAuthorityInput;
import com.tth.auth.service.ResourceAuthorityService;
import com.tth.auth.utils.CurrentUserContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@Validated
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(value = "resource-authority")
public class ResourceAuthorityController {

  @Autowired
  private ResourceAuthorityService resourceAuthorityService;

  @PostMapping("grant")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void grant(@RequestBody @Valid ResourceAuthorityInput input) {
    UserAuthority currentUser = CurrentUserContext.get();
    
    resourceAuthorityService.authenticate(ResourceAccessCredential.builder()
        .targetIds(currentUser.getResourceAuthorities())
        .resourceType(input.getTargetType())
        .resourceId(input.getTargetId().toString())
        .permission(ResourcePermission.GRANT_PERMISSION)
        .build());
    
    resourceAuthorityService.authenticate(ResourceAccessCredential.builder()
        .targetIds(currentUser.getResourceAuthorities())
        .resourceType(input.getResourceType())
        .resourceId(input.getResourceId())
        .permission(ResourcePermission.GRANT_PERMISSION)
        .build());
    
    resourceAuthorityService.grant(input);
  }

  @PostMapping("remove")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void remove(@RequestBody @Valid ResourceAuthorityInput input) {
    UserAuthority currentUser = CurrentUserContext.get();
    
    resourceAuthorityService.authenticate(ResourceAccessCredential.builder()
        .targetIds(currentUser.getResourceAuthorities())
        .resourceType(input.getTargetType())
        .resourceId(input.getTargetId().toString())
        .permission(ResourcePermission.GRANT_PERMISSION)
        .build());
    
    resourceAuthorityService.authenticate(ResourceAccessCredential.builder()
        .targetIds(currentUser.getResourceAuthorities())
        .resourceType(input.getResourceType())
        .resourceId(input.getResourceId())
        .permission(ResourcePermission.GRANT_PERMISSION)
        .build());
    
    resourceAuthorityService.remove(input);
  }

  @GetMapping
  public List<ResourceAuthorityData> getList(@Valid ResourceAuthorityCriteria criteria, 
      @SortDefault(sort = {"resourceType", "targetType"}) Sort sort
  ) {
    UserAuthority currentUser = CurrentUserContext.get();
    
    if (criteria.getTargetId() != null) {
      resourceAuthorityService.authenticate(ResourceAccessCredential.builder()
          .targetIds(currentUser.getResourceAuthorities())
          .resourceType(criteria.getTargetType())
          .resourceId(criteria.getTargetId().toString())
          .permission(ResourcePermission.GRANT_PERMISSION)
          .build());
    }
    if (criteria.getResourceId() != null) {
      resourceAuthorityService.authenticate(ResourceAccessCredential.builder()
          .targetIds(currentUser.getResourceAuthorities())
          .resourceType(criteria.getResourceType())
          .resourceId(criteria.getResourceId())
          .permission(ResourcePermission.GRANT_PERMISSION)
          .build());
    }
    
    return resourceAuthorityService.getList(criteria, sort);
  }

}
