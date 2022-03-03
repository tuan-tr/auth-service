package com.tth.auth.repository;

import java.util.Optional;

import javax.validation.constraints.NotBlank;

import com.tth.auth.entity.User;
import com.tth.auth.repository.custom.CustomUserRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.validation.annotation.Validated;

@Validated
public interface UserRepository extends JpaRepository<User, String>, CustomUserRepository {

  Optional<User> findByUsername(@NotBlank String username);
  
  @Query("SELECT u FROM User u"
  + " LEFT JOIN FETCH u.personalInformation pi"
  + " LEFT JOIN FETCH u.modifiedBy mb"
  + " WHERE u.id = :id")
  Optional<User> findInforById(@Param("id") @NotBlank String id);
  
}
