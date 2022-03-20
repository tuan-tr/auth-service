package com.tth.auth.configuration.security.aspect;

import java.util.List;

import com.tth.auth.configuration.security.annotation.ResourceAuthentication;
import com.tth.auth.configuration.security.annotation.ResourceAuthentication.ResourceAuthentications;
import com.tth.auth.configuration.security.user.UserAuthority;
import com.tth.auth.dto.resourceAuthority.ResourceAccessCredential;
import com.tth.auth.exception.ResourcePermissionNotFoundException;
import com.tth.auth.service.ResourceAuthorityService;
import com.tth.auth.util.CurrentUserContext;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ResourceAuthenticationAspect {

  @Autowired
  private ResourceAuthorityService resourceAuthorityService;
  
  @Before("@annotation(resourceAuthentications)")
  public void verifyPermissionOnResource(JoinPoint joinPoint,
      ResourceAuthentications resourceAuthentications
  ) throws Throwable {
    for (ResourceAuthentication resourceAuthentication : resourceAuthentications.value()) {
      verifyPermissionOnResource(joinPoint, resourceAuthentication);
    }
  }
  
  @Before("@annotation(resourceAuthentication)")
  public void verifyPermissionOnResource(JoinPoint joinPoint,
      ResourceAuthentication resourceAuthentication
  ) throws Throwable {
    String resourceId = null;
    if (resourceAuthentication.resourceId().equals("") == false) {
      Expression expression = new SpelExpressionParser()
          .parseExpression(resourceAuthentication.resourceId());

      resourceId = expression.getValue(joinPoint).toString();
    }

    UserAuthority currentUser = CurrentUserContext.find()
        .orElseThrow(() -> new AuthenticationCredentialsNotFoundException(
            "No current user associated with this request"));

    ResourceAccessCredential credential = ResourceAccessCredential.builder()
        .resourceType(resourceAuthentication.resourceType())
        .resourceId(resourceId)
        .targetIds(currentUser.getResourceAuthorities())
        .permissions(List.of(resourceAuthentication.permissions()))
        .build();

    if (resourceAuthorityService.hasPermission(credential) == false) {
      throw new ResourcePermissionNotFoundException(credential.getResourceType(),
          credential.getResourceId(), credential.getPermissions());
    }
  }

}
