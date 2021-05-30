package com.tth.auth.configuration.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tth.auth.configuration.security.token.TokenProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {

  @Autowired
  private TokenProvider tokenProvider;

  @Autowired
  private UserDetailsService userService;

  @Override
  protected void doFilterInternal(HttpServletRequest request,
      HttpServletResponse response, FilterChain filterChain
  ) throws ServletException, IOException {
    String token = null;
    String bearerToken = request.getHeader("Authorization");
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
      token = bearerToken.substring(7);
    }

    if (StringUtils.hasText(token)) {
      UserDetails currentUser = null;
      try {
        currentUser = tokenProvider.parseUserDetail(token);
      } catch (Exception e) {
        throw new AuthenticationException(e.getMessage(), e){};
      }

      currentUser = userService.loadUserByUsername(currentUser.getUsername());

      if (currentUser.isEnabled() == false) {
        throw new DisabledException("User is disabled");
      }

      UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
          currentUser, null, currentUser.getAuthorities());
      authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
      SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    filterChain.doFilter(request, response);
  }

}
