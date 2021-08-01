package com.tth.auth.configuration.security.user;

import java.util.Collection;

import com.tth.auth.entity.User;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserAuthority implements UserDetails {

  private String id;
  private Collection<String> resourceAuthorities;
  private User userEntity;

  private Collection<GrantedAuthority> authorities;
  private String password;
  private String username;
  private boolean accountNonExpired;
  private boolean accountNonLocked;
  private boolean credentialsNonExpired;
  private boolean enabled;

}
