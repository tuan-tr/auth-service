package com.tth.auth.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.tth.auth.constant.ResourceType;
import com.tth.auth.entity.ResourceAuthority;
import com.tth.auth.repository.custom.CustomResourceAuthorityRepository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.validation.annotation.Validated;

@Validated
public interface ResourceAuthorityRepository extends JpaRepository<ResourceAuthority, String>, CustomResourceAuthorityRepository {
  
  long deleteByTargetTypeAndTargetId(ResourceType targetType, String targetId);
  
  long deleteByResourceTypeAndResourceId(ResourceType resourceType, String resourceId);
  
  @Query(value =
    "SELECT ra FROM ResourceAuthority ra "
  + "WHERE resourceType = :resourceType "
  + "AND (resourceId IS NULL OR resourceId = :resourceId) "
  + "AND targetId IN (:targetIds) "
  + "AND bitwise_and(permissions, :permissions) = :permissions ")
  List<ResourceAuthority> findOnSpecificResource(
      @Param("resourceType") @NotNull ResourceType resourceType,
      @Param("resourceId") @NotBlank String resourceId,
      @Param("targetIds") @NotEmpty Collection<String> targetIds,
      @Param("permissions") @NotNull int permissions,
      Pageable pageable);

  @Query(value =
    "SELECT ra FROM ResourceAuthority ra "
  + "WHERE resourceType = :resourceType "
  + "AND resourceId IS NULL "
  + "AND targetId IN (:targetIds) "
  + "AND bitwise_and(permissions, :permissions) = :permissions ")
  List<ResourceAuthority> findOnResourceType(
      @Param("resourceType") @NotNull ResourceType resourceType,
      @Param("targetIds") @NotEmpty Collection<String> targetIds,
      @Param("permissions") @NotNull int permissions,
      Pageable pageable);
  
  @Query(nativeQuery = true, value =
    " SELECT ra.id FROM {h-schema}resource_authority ra"
  + " WHERE ra.resource_type = :resourceType"
  + " AND (ra.resource_id IS NULL OR ra.resource_id = :resourceId)"
  + " AND ra.target_id IN (:targetIds)"
  + " AND ra.permissions & :permissions = :permissions"
  + " LIMIT 1")
  String findFirstIdMatchToSpecificResource(
      @Param("resourceType") @NotBlank String resourceType,
      @Param("resourceId") @NotBlank String resourceId,
      @Param("targetIds") @NotEmpty Collection<String> targetIds,
      @Param("permissions") @NotNull int permissions);
  
  @Query(nativeQuery = true, value =
    " SELECT ra.id FROM {h-schema}resource_authority ra"
  + " WHERE ra.resource_type = :resourceType"
  + " AND ra.resource_id IS NULL"
  + " AND ra.target_id IN (:targetIds)"
  + " AND ra.permissions & :permissions = :permissions"
  + " LIMIT 1")
  String findFirstIdMatchToResourceType(
      @Param("resourceType") @NotBlank String resourceType,
      @Param("targetIds") @NotEmpty Collection<String> targetIds,
      @Param("permissions") @NotNull int permissions);
  
  @Query(nativeQuery = true, value =
    " SELECT DISTINCT ra.resource_id FROM {h-schema}resource_authority ra"
  + " WHERE ra.resource_type = :resourceType"
  + " AND ra.target_id IN (:targetIds)"
  + " AND ra.permissions & :permissions = :permissions")
  List<String> findResourceIdsMatchToTargets(
      @Param("resourceType") @NotBlank String resourceType,
      @Param("targetIds") @NotEmpty Collection<String> targetIds,
      @Param("permissions") @NotNull int permissions);
  
  @Query(value =
    " SELECT ra FROM ResourceAuthority ra"
  + " WHERE ra.targetType = :targetType"
  + " AND ra.targetId = :targetId"
  + " AND ra.resourceType = :resourceType"
  + " AND ((:resourceId IS NULL AND ra.resourceId IS NULL)"
  + "   OR ra.resourceId = :resourceId)")
  Optional<ResourceAuthority> findOne(
      @Param("targetType") @NotNull ResourceType targetType,
      @Param("targetId") @NotBlank String targetId,
      @Param("resourceType") @NotNull ResourceType resourceType,
      @Param("resourceId") String resourceId);
  
  @Query(value =
    " SELECT ra FROM ResourceAuthority ra"
  + " LEFT JOIN FETCH ra.targetUser tu"
  + " LEFT JOIN FETCH ra.targetGroup tg"
  + " LEFT JOIN FETCH ra.resourceUser ru"
  + " LEFT JOIN FETCH ra.resourceGroup rg"
  + " LEFT JOIN FETCH ra.modifiedBy mb"
  + " WHERE ra.id IN (:ids)")
  List<ResourceAuthority> findByIds(@Param("ids") @NotEmpty Collection<String> ids);
  
}
