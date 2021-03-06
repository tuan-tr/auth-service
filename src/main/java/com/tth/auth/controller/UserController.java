package com.tth.auth.controller;

import javax.validation.Valid;

import com.tth.auth.configuration.security.annotation.ResourceAuthentication;
import com.tth.auth.constant.ResourcePermission;
import com.tth.auth.constant.ResourceType;
import com.tth.auth.dto.personalInformation.PersonalInformationInput;
import com.tth.auth.dto.user.UserCriteria;
import com.tth.auth.dto.user.UserDto;
import com.tth.auth.dto.user.UserInput;
import com.tth.auth.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@Validated
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(value = "user")
public class UserController {
  
  @Autowired
  private UserService userService;
  
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @ResourceAuthentication(resourceType = ResourceType.USER,
      permissions = ResourcePermission.CREATE)
  public UserDto create(@RequestBody @Valid UserInput input) {
    return userService.create(input);
  }
  
  @GetMapping("{id}")
  @ResourceAuthentication(resourceType = ResourceType.USER,
      permissions = ResourcePermission.READ,
      resourceId = "args[0]")
  public UserDto getInforById(@PathVariable("id") String id) {
    return userService.getInforById(id);
  }
  
  @GetMapping
  public Page<UserDto> getList(@Valid UserCriteria criteria, Pageable pageable) {
    return userService.getList(criteria, pageable);
  }
  
  @PutMapping("{id}/enabled/{enabled}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @ResourceAuthentication(resourceType = ResourceType.USER,
      permissions = ResourcePermission.ENABLE,
      resourceId = "args[0]")
  public void enable(@PathVariable("id") String id,
      @PathVariable("enabled") boolean enabled
  ) {
    userService.enable(id, enabled);
  }
  
  @PutMapping("{id}/information")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @ResourceAuthentication(resourceType = ResourceType.USER,
      permissions = ResourcePermission.UPDATE,
      resourceId = "args[0]")
  public void updateInformation(@PathVariable("id") String id,
      @RequestBody @Valid PersonalInformationInput input
  ) {
    userService.updateInformation(id, input);
  }
  
}
