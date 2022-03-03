package com.tth.auth.repository;

import java.util.Optional;

import javax.validation.constraints.NotBlank;

import com.tth.auth.entity.Group;
import com.tth.auth.repository.custom.CustomGroupRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.validation.annotation.Validated;

@Validated
public interface GroupRepository extends JpaRepository<Group, String>, CustomGroupRepository {
  
  @Query("SELECT g FROM Group g"
  + " LEFT JOIN FETCH g.createdBy cb"
  + " LEFT JOIN FETCH cb.personalInformation"
  + " WHERE g.id = :id")
  Optional<Group> findIncludeCreator(@Param("id") @NotBlank String id);
  
}
