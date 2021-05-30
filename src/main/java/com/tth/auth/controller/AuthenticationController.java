package com.tth.auth.controller;

import javax.validation.Valid;

import com.tth.auth.dto.authentication.SigninRequest;
import com.tth.auth.dto.authentication.SignupRequest;
import com.tth.auth.service.AuthenticationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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

}
