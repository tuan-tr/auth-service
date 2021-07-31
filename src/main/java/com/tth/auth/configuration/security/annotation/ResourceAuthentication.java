package com.tth.auth.configuration.security.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.tth.auth.configuration.security.annotation.ResourceAuthentication.ResourceAuthentications;
import com.tth.auth.constant.ResourcePermission;
import com.tth.auth.constant.ResourceType;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(ResourceAuthentications.class)
public @interface ResourceAuthentication {

  ResourceType resourceType();
  ResourcePermission[] permissions();
  String resourceId() default "";

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  public @interface ResourceAuthentications {
    ResourceAuthentication[] value();
  }

}
