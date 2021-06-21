package com.tth.auth.configuration;

import java.util.Map;
import java.util.TreeMap;

import com.tth.auth.configuration.setting.UserSetting;

import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Schema;

@Configuration
@EnableJpaAuditing(
    auditorAwareRef = "auditorAware",
    dateTimeProviderRef = "dateTimeProvider")
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer")
public class BeanConfigurer {
  
  @Bean
  public String defaultUserPassword(
    @Value("${setting.default-user-password}") String defaultUserPassword
  ) {
    return defaultUserPassword;
  }
  
  @Bean
  @ConfigurationProperties(prefix = "setting.admin-user")
  public UserSetting adminUserSetting() {
    return new UserSetting();
  }
  
  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .info(new Info()
            .title("Auth Service API")
            .version("0.1")
            .description("Auth Service API document")
        );
  }
  
  @Bean
  @SuppressWarnings("rawtypes")
  public OpenApiCustomiser sortSchemasAlphabetically() {
    return openApi -> {
      Map<String, Schema> schemas = openApi.getComponents().getSchemas();
      openApi.getComponents().setSchemas(new TreeMap<>(schemas));
    };
  }
  
}
