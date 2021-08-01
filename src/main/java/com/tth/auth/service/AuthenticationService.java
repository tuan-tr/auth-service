package com.tth.auth.service;

import com.tth.auth.configuration.security.token.TokenProvider;
import com.tth.auth.configuration.security.user.UserAuthority;
import com.tth.auth.constant.ResourcePermission;
import com.tth.auth.constant.ResourceType;
import com.tth.auth.dto.authentication.Credentials;
import com.tth.auth.dto.authentication.SigninRequest;
import com.tth.auth.dto.authentication.SignupRequest;
import com.tth.auth.dto.user.UserDTO;
import com.tth.auth.entity.ResourceAuthority;
import com.tth.auth.entity.User;
import com.tth.auth.exception.DuplicateKeyException;
import com.tth.auth.repository.ResourceAuthorityRepository;
import com.tth.auth.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AuthenticationService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ResourceAuthorityRepository resourceAuthorityRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private TokenProvider tokenProvider;

  @Transactional
  public UserDTO signup(SignupRequest request) {
    userRepository.findByUsername(request.getUsername())
        .ifPresent(user -> {
          throw new DuplicateKeyException("username", request.getUsername());
        });

    User user = User.builder()
        .username(request.getUsername())
        .password(passwordEncoder.encode(request.getPassword()))
        .build();
    user.setCreatedBy(user);
    user.setModifiedBy(user);
    userRepository.save(user);

    ResourceAuthority resourceAuthority = ResourceAuthority.builder()
        .targetType(ResourceType.USER)
        .targetId(user.getId())
        .resourceType(ResourceType.USER)
        .resourceId(user.getId())
        .permissions(ResourcePermission.READ.getCode()
            + ResourcePermission.UPDATE.getCode())
        .modifiedBy(user)
        .build();
    resourceAuthorityRepository.save(resourceAuthority);

    return UserDTO.builder()
        .id(user.getId())
        .build();
  }

  public Credentials signin(SigninRequest request) {
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getUsername(), request.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    UserAuthority currentUser = (UserAuthority) authentication.getPrincipal();

    String token = tokenProvider.generateToken(currentUser);
    return Credentials.builder()
        .accessToken(token)
        .build();
  }

}
