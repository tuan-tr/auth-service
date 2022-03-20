package com.tth.auth.configuration;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.tth.auth.dto.mixIn.BindExceptionMixIn;
import com.tth.auth.dto.mixIn.CustomExceptionMixIn;
import com.tth.auth.dto.mixIn.PageMixIn;
import com.tth.auth.exception.CustomException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.validation.FieldError;

@Configuration
public class JacksonConfigurer {
  
  @Bean
  public Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder() {
    return new Jackson2ObjectMapperBuilder()
        .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,
            SerializationFeature.FAIL_ON_EMPTY_BEANS)
        .mixIn(Page.class, PageMixIn.class)
        .mixIn(FieldError.class, BindExceptionMixIn.class)
        .mixIn(CustomException.class, CustomExceptionMixIn.class)
        ;
  }
  
}
