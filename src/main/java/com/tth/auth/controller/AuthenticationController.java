package com.tth.auth.controller;

import javax.validation.Valid;

import com.tth.auth.configuration.security.annotation.ResourceAuthentication;
import com.tth.auth.configuration.security.user.UserAuthority;
import com.tth.auth.constant.ResourcePermission;
import com.tth.auth.constant.ResourceType;
import com.tth.auth.dto.authentication.Credentials;
import com.tth.auth.dto.authentication.SigninRequest;
import com.tth.auth.dto.authentication.SignupRequest;
import com.tth.auth.dto.user.UserDTO;
import com.tth.auth.service.AuthenticationService;
import com.tth.auth.util.CurrentUserContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@Validated
public class AuthenticationController {

  @Autowired
  private AuthenticationService authenticationService;

  @PostMapping("/signup")
  public UserDTO signup(@RequestBody @Valid SignupRequest request) {
    return authenticationService.signup(request);
  }

  @PostMapping("/signin")
  public Credentials signin(@RequestBody @Valid SigninRequest request) {
    return authenticationService.signin(request);
  }

  // @GetMapping("/test/{id}")
  @SecurityRequirement(name = "bearerAuth")
  @ResourceAuthentication(resourceType = ResourceType.USER,
      permissions = ResourcePermission.READ,
      resourceId = "args[1]")
  public Object test(Authentication authentication,
      @PathVariable(name = "id") String id
  ) {
    UserAuthority currentUser = CurrentUserContext.get();
    return null;
  }

}
