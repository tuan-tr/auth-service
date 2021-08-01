package com.tth.auth.configuration.security.token;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import com.tth.auth.configuration.security.user.UserAuthority;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

// TODO more implementation
@Component
public class TokenProvider {

  private final String JWT_SECRET = "jweow4wnf9";
  private final long JWT_EXPIRATION = 1000*60*60*24;

  public String generateToken(UserAuthority user) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);

    Map<String,Object> claims = new LinkedHashMap<>();
    claims.put("userId", user.getId());
    claims.put("username", user.getUsername());

    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(expiryDate)
        .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
        .compact();
  }

  public UserAuthority parseUserDetail(String token) {
    Claims claims = Jwts.parser()
        .setSigningKey(JWT_SECRET)
        .parseClaimsJws(token)
        .getBody();

    String userId = claims.get("userId").toString();
    String username = claims.get("username").toString();

    return UserAuthority.builder()
        .id(userId)
        .username(username)
        .build();
  }

}
