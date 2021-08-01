package com.tth.auth.util;

import java.util.Optional;

import com.tth.auth.configuration.security.user.UserAuthority;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class CurrentUserContext {
  
  public static Optional<UserAuthority> find() {
    return Optional.ofNullable(SecurityContextHolder.getContext())
        .map(SecurityContext::getAuthentication)
        .filter(Authentication::isAuthenticated)
        .map(Authentication::getPrincipal)
        .filter(principal -> principal instanceof UserAuthority)
        .map(UserAuthority.class::cast);
  }
  
  public static UserAuthority get() {
    return find()
        .orElseThrow(() -> new AuthenticationCredentialsNotFoundException(
            "No current user associated with this request"));
  }
  
}
