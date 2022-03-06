package com.tth.auth.configuration;

import java.util.Map;

import com.tth.auth.configuration.hibernate.CustomPostgreSQLDialect;

import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(
    auditorAwareRef = "auditorAware",
    dateTimeProviderRef = "dateTimeProvider")
public class HibernateConfigurer implements HibernatePropertiesCustomizer {

  @Override
  public void customize(Map<String, Object> hibernateProperties) {
    hibernateProperties.put("hibernate.dialect", CustomPostgreSQLDialect.class.getName());
  }
  
}
