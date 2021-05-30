package com.tth.auth.configuration.jpa;

import java.util.Optional;

import com.tth.auth.configuration.security.user.UserAuthority;
import com.tth.auth.entity.User;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("auditorAware")
public class AuditorAwareImpl implements AuditorAware<User> {

  @Override
  public Optional<User> getCurrentAuditor() {
    return Optional.ofNullable(SecurityContextHolder.getContext())
        .map(SecurityContext::getAuthentication)
        .filter(Authentication::isAuthenticated)
        .map(Authentication::getPrincipal)
        .filter(principal -> principal instanceof UserAuthority)
        .map(UserAuthority.class::cast)
        .map(UserAuthority::getUserEntity);
  }

}
