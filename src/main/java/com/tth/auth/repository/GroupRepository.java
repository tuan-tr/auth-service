package com.tth.auth.repository;

import java.util.Optional;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.tth.auth.entity.Group;
import com.tth.auth.repository.custom.CustomGroupRepository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.validation.annotation.Validated;

@Validated
public interface GroupRepository extends JpaRepository<Group, String>, CustomGroupRepository {
  
  @Query("SELECT gr FROM Group gr WHERE gr.id = :id")
  @EntityGraph(attributePaths = {"createdBy", "createdBy.personalInformation"})
  <T> Optional<T> findDataById(@NotBlank String id, @NotNull Class<T> type);
  
}
