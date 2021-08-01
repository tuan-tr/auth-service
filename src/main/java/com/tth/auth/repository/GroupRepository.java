package com.tth.auth.repository;

import java.util.Collection;
import java.util.Optional;

import javax.validation.constraints.NotNull;

import com.tth.auth.entity.Group;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.validation.annotation.Validated;

@Validated
public interface GroupRepository extends JpaRepository<Group, String> {
  
  @Query(value =
    "SELECT gr FROM Group gr "
  + "WHERE gr.id = :id ")
  @EntityGraph(attributePaths = {"createdBy", "createdBy.personalInformation"})
  <T> Optional<T> findDataById(@Param("id") @NotNull String id, @NotNull Class<T> type);
  
  @Query(value =
    "SELECT gr FROM Group gr "
  + "WHERE (COALESCE(:ids) IS NULL OR gr.id IN (:ids)) "
  + "AND (:enabled IS NULL OR gr.enabled = :enabled) "
  + "AND (:keyword IS NULL OR gr.name LIKE %:keyword%) ")
  @EntityGraph(attributePaths = {"createdBy", "createdBy.personalInformation"})
  <T> Page<T> findList(
      @Param("ids") Collection<String> ids,
      @Param("enabled") Boolean enabled,
      @Param("keyword") String keyword,
      Pageable pageable,
      @NotNull Class<T> type);
  
}
