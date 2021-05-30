package com.tth.auth.configuration;

import com.tth.auth.configuration.setting.UserSetting;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(
    auditorAwareRef = "auditorAware",
    dateTimeProviderRef = "dateTimeProvider")
public class BeanConfigurer {

  @Bean
  @ConfigurationProperties(prefix = "setting.admin-user")
  UserSetting adminUserSetting() {
    return new UserSetting();
  }

  @Bean
  @ConfigurationProperties(prefix = "setting.default-user-password")
  String defaultUserPassword() {
    return new String();
  }

}
