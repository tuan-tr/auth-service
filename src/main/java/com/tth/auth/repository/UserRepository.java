package com.tth.auth.repository;

import java.util.Collection;
import java.util.Optional;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.tth.auth.entity.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.validation.annotation.Validated;

@Validated
public interface UserRepository extends JpaRepository<User, String> {

  Optional<User> findByUsername(@NotBlank String username);
  
  @Query(value =
    "SELECT us FROM User us "
  + "WHERE us.id = :id "
  + "AND us.rootUser = false ")
  @EntityGraph(attributePaths = {"personalInformation"})
  <T> Optional<T> findNonRootInforById(@Param("id") @NotNull String id, @NotNull Class<T> type);
  
  @Query(value =
    "SELECT us FROM User us "
  + "WHERE us.id = :id ")
  @EntityGraph(attributePaths = {"personalInformation"})
  <T> Optional<T> findInforById(@Param("id") @NotNull String id, @NotNull Class<T> type);
  
  @Query(value =
    "SELECT us FROM User us "
  + "LEFT JOIN us.personalInformation pi "
  + "WHERE (COALESCE(:ids) IS NULL OR us.id IN (:ids)) "
  + "AND (:enabled IS NULL OR us.enabled = :enabled) "
  + "AND (:gender IS NULL OR pi.gender = :gender) "
  + "AND (:keyword IS NULL OR us.username LIKE %:keyword% "
  + "  OR CONCAT(pi.lastName, ' ', pi.firstName) LIKE %:keyword%) ")
  @EntityGraph(attributePaths = {"personalInformation"})
  <T> Page<T> findList(
      @Param("ids") Collection<String> ids,
      @Param("enabled") Boolean enabled,
      @Param("keyword") String keyword,
      @Param("gender") Boolean gender,
      Pageable pageable,
      @NotNull Class<T> type);
  
}
