package com.tth.auth.configuration;

import com.tth.auth.configuration.setting.UserEnvironment;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EnvironmentConfigurer {
  
  @Bean
  public String defaultUserPassword(@Value("${environment.default-user-password}") String defaultUserPassword) {
    return defaultUserPassword;
  }
  
  @Bean
  @ConfigurationProperties(prefix = "environment.admin-user")
  public UserEnvironment adminUserEnvironment() {
    return new UserEnvironment();
  }
  
  @Bean
  public String[] nonAuthenticatedPaths(@Value("${environment.non-authenticated-paths}") String[] nonAuthenticatedPaths) {
    return nonAuthenticatedPaths;
  }
  
}
