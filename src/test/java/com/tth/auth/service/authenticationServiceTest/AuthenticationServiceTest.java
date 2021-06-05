package com.tth.auth.service.authenticationServiceTest;

import com.tth.auth.configuration.security.token.TokenProvider;
import com.tth.auth.repository.ResourceAuthorityRepository;
import com.tth.auth.repository.UserRepository;
import com.tth.auth.service.AuthenticationService;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class AuthenticationServiceTest {
  
  @TestConfiguration
  static class Configuration {
    @Bean
    AuthenticationService authenticationService() {
      return new AuthenticationService();
    }
  }
  
  @Autowired
  private AuthenticationService authenticationService;
  
  @MockBean
  private UserRepository userRepository;
  
  @MockBean
  private ResourceAuthorityRepository resourceAuthorityRepository;
  
  @MockBean
  private PasswordEncoder passwordEncoder;
  
  @MockBean
  private AuthenticationManager authenticationManager;
  
  @MockBean
  private TokenProvider tokenProvider;
  
}
