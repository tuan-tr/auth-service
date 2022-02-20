package com.tth.auth.repository;

import java.util.Optional;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.tth.auth.entity.User;
import com.tth.auth.repository.custom.CustomUserRepository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.validation.annotation.Validated;

@Validated
public interface UserRepository extends JpaRepository<User, String>, CustomUserRepository {

  Optional<User> findByUsername(@NotBlank String username);
  
  @Query("SELECT u FROM User u WHERE u.id = :id")
  @EntityGraph(attributePaths = {"personalInformation"})
  <T> Optional<T> findInforById(@NotBlank String id, @NotNull Class<T> type);
  
}
