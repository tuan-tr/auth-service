package com.tth.auth.repository;

import java.util.Optional;
import java.util.UUID;

import javax.validation.constraints.NotBlank;

import com.tth.auth.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.validation.annotation.Validated;

@Validated
public interface UserRepository extends JpaRepository<User, UUID> {

  Optional<User> findByUsername(@NotBlank String username);

}
