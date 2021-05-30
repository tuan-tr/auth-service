package com.tth.auth.controller;

import java.util.UUID;

import javax.validation.Valid;

import com.tth.auth.configuration.security.annotation.ResourceAuthentication;
import com.tth.auth.dto.authentication.SigninRequest;
import com.tth.auth.dto.authentication.SignupRequest;
import com.tth.auth.dto.resourceAuthority.ResourcePermission;
import com.tth.auth.dto.resourceAuthority.ResourceType;
import com.tth.auth.service.AuthenticationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping
public class AuthenticationController {

  @Autowired
  private AuthenticationService authenticationService;

  @PostMapping("/signup")
  public ResponseEntity<?> signup(@RequestBody @Valid SignupRequest request) {
    return ResponseEntity.ok(authenticationService.signup(request));
  }

  @PostMapping("/signin")
  public ResponseEntity<?> signin(@RequestBody @Valid SigninRequest request) {
    return ResponseEntity.ok(authenticationService.signin(request));
  }

  @GetMapping("/test/{userId}")
  @ResourceAuthentication(resourceType = ResourceType.USER,
      permissions = ResourcePermission.READ,
      resourceId = "args[1]")
  public ResponseEntity<?> test(Authentication authentication,
      @PathVariable(name = "userId", required = false) UUID userId
  ) {
    return ResponseEntity.ok(authenticationService.getCurrentUser()
        .orElseThrow(() -> new AuthenticationCredentialsNotFoundException(
            "No current user associated with this request"))
        );
  }

}
