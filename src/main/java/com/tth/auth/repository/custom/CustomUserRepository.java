package com.tth.auth.repository.custom;

import java.util.Collection;

import com.tth.auth.dto.user.UserCriteria;
import com.tth.auth.entity.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomUserRepository {
  
  Page<User> findList(Collection<String> ids, UserCriteria criteria, Pageable pageable);
  
}
