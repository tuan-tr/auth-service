package com.tth.auth.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.tth.auth.configuration.security.user.UserAuthority;
import com.tth.auth.entity.User;
import com.tth.auth.repository.GroupMemberRepository;
import com.tth.auth.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserService implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private GroupMemberRepository groupMemberRepository;

  @Override
  public UserDetails loadUserByUsername(String username) {
    User user = userRepository.findByUsername(username)
        .orElseThrow(()-> new UsernameNotFoundException(username));

    List<UUID> groupIds = groupMemberRepository.findEnabledGroupIds(user.getId());
    List<UUID> resourceAuthorities = new ArrayList<>(groupIds);
    resourceAuthorities.add(user.getId());

    return UserAuthority.builder()
        .id(user.getId())
        .resourceAuthorities(resourceAuthorities)
        .userEntity(user)
        .username(username)
        .password(user.getPassword())
        .accountNonExpired(true)
        .accountNonLocked(true)
        .credentialsNonExpired(true)
        .enabled(user.isEnabled())
        .build();
  }

}
