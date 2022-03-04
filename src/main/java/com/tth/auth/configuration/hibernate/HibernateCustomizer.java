package com.tth.auth.configuration.hibernate;

import java.util.Map;

import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HibernateCustomizer implements HibernatePropertiesCustomizer {

  @Override
  public void customize(Map<String, Object> hibernateProperties) {
    hibernateProperties.put("hibernate.dialect", CustomPostgreSQLDialect.class.getCanonicalName());
  }
  
}
