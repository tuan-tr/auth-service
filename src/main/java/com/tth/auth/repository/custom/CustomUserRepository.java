package com.tth.auth.repository.custom;

import java.util.Collection;

import com.tth.auth.dto.user.UserCriteria;
import com.tth.auth.dto.user.UserDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomUserRepository {
  
  Page<UserDTO> findList(Collection<String> ids, UserCriteria criteria, Pageable pageable);
  
}
