package com.tth.auth.repository.custom.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.tth.auth.constant.ResourcePermission;
import com.tth.auth.dto.resourceAuthority.ResourceAuthorityCriteria;
import com.tth.auth.repository.custom.CustomResourceAuthorityRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class CustomResourceAuthorityRepositoryImpl implements CustomResourceAuthorityRepository {
  
  @PersistenceContext
  private EntityManager entityManager;
  
  @Override
  public Page<String> findIdsMatchGrantPermission(Collection<String> verifingTargetIds, ResourceAuthorityCriteria criteria, Pageable pageable) {
    if (criteria.isUnpaged()) {
      return this.findUnpagedIdsMatchGrantPermission(verifingTargetIds, criteria, pageable.getSort());
    }
    return this.findPagedIdsMatchGrantPermission(verifingTargetIds, criteria, pageable);
  }
  
  private Page<String> findUnpagedIdsMatchGrantPermission(Collection<String> verifingTargetIds, ResourceAuthorityCriteria criteria, Sort sort) {
    String whereClause = this.makeFindIdsMatchGrantPermissionWhereClauseSql(verifingTargetIds, criteria);
    String orderClause = sort.isSorted() ? this.makeFindIdsMatchGrantPermissionOrderClauseSql(sort) : "";
    
    StringBuilder sqlBuilder = new StringBuilder(800)
        .append("SELECT ra.id FROM {h-schema}resource_authority ra")
        .append(whereClause)
        .append(orderClause);
    
    Query query = entityManager.createNativeQuery(sqlBuilder.toString());
    this.setFindIdsMatchGrantPermissionQueryParameter(query, verifingTargetIds, criteria);
    
    List<String> entities = query.getResultList();
    return new PageImpl<>(entities);
  }
  
  private Page<String> findPagedIdsMatchGrantPermission(Collection<String> verifingTargetIds, ResourceAuthorityCriteria criteria, Pageable pageable) {
    String whereClause = this.makeFindIdsMatchGrantPermissionWhereClauseSql(verifingTargetIds, criteria);
    
    StringBuilder countSqlBuilder = new StringBuilder(800)
        .append("SELECT COUNT(ra.id) FROM {h-schema}resource_authority ra")
        .append(whereClause);
    
    Query countQuery = entityManager.createNativeQuery(countSqlBuilder.toString());
    this.setFindIdsMatchGrantPermissionQueryParameter(countQuery, verifingTargetIds, criteria);
    Long total = Long.valueOf(countQuery.getSingleResult().toString());
    
    if (total == 0) {
      return new PageImpl<>(Collections.emptyList(), pageable, 0);
    }
    
    String orderClause = pageable.getSort().isSorted() ? 
        this.makeFindIdsMatchGrantPermissionOrderClauseSql(pageable.getSort()) : "";
    
    StringBuilder pageSqlBuilder = new StringBuilder(800)
        .append("SELECT ra.id FROM {h-schema}resource_authority ra")
        .append(whereClause)
        .append(orderClause);
    
    Query pageQuery = entityManager.createNativeQuery(pageSqlBuilder.toString())
        .setFirstResult((int) pageable.getOffset())
        .setMaxResults(pageable.getPageSize());
    this.setFindIdsMatchGrantPermissionQueryParameter(pageQuery, verifingTargetIds, criteria);
    
    List<String> entities = pageQuery.getResultList();
    return new PageImpl<>(entities, pageable, total);
  }

  private String makeFindIdsMatchGrantPermissionWhereClauseSql(Collection<String> verifingTargetIds, ResourceAuthorityCriteria criteria) {
    StringBuilder sqlBuilder = new StringBuilder(800);
    if (criteria.getTargetId() == null) {
      sqlBuilder
      .append(" RIGHT JOIN (")
      .append("   SELECT DISTINCT ratg.resource_id, ratg.resource_type")
      .append("   FROM {h-schema}resource_authority ratg")
      .append("   WHERE ratg.permissions & :grantPermission = :grantPermission")
      .append("   AND ratg.target_id IN (:verifingTargetIds)")
      .append(criteria.getTargetType() == null ? "" :
              "   AND ratg.resource_type = :targetType")
      .append(" ) AS ratg ON ratg.resource_type = ra.target_type")
      .append("   AND (ratg.resource_id IS NULL OR ratg.resource_id = ra.target_id)");
    }
    if (criteria.getResourceId() == null) {
      sqlBuilder
      .append(" RIGHT JOIN (")
      .append("   SELECT DISTINCT rars.resource_id, rars.resource_type")
      .append("   FROM {h-schema}resource_authority rars")
      .append("   WHERE rars.permissions & :grantPermission = :grantPermission")
      .append("   AND rars.target_id IN (:verifingTargetIds)")
      .append(criteria.getResourceType() == null ? "" :
              "   AND rars.resource_type = :resourceType")
      .append(" ) AS rars ON rars.resource_type = ra.resource_type")
      .append("   AND (rars.resource_id IS NULL OR rars.resource_id = ra.resource_id)");
    }
    sqlBuilder.append(" WHERE 1=1");
    if (criteria.getTargetType() != null) {
      sqlBuilder.append(" AND ra.target_type = :targetType");
    }
    if (criteria.getTargetId() != null) {
      sqlBuilder.append(" AND ra.target_id = :targetId");
    }
    if (criteria.getResourceType() != null) {
      sqlBuilder.append(" AND ra.resource_type = :resourceType");
    }
    if (criteria.getResourceId() != null) {
      sqlBuilder.append(" AND ra.resource_id = :resourceId");
    }
    if (criteria.getPermissions() != null) {
      sqlBuilder.append(" AND ra.permissions & :permissions = :permissions");
    }
    return sqlBuilder.toString();
  }

  private void setFindIdsMatchGrantPermissionQueryParameter(Query query, Collection<String> verifingTargetIds, ResourceAuthorityCriteria criteria) {
    if (criteria.getTargetId() == null || criteria.getResourceId() == null) {
      query.setParameter("verifingTargetIds", verifingTargetIds);
      query.setParameter("grantPermission", ResourcePermission.GRANT_PERMISSION.getCode());
    }
    if (criteria.getTargetType() != null) {
      query.setParameter("targetType", criteria.getTargetType().toString());
    }
    if (criteria.getTargetId() != null) {
      query.setParameter("targetId", criteria.getTargetId());
    }
    if (criteria.getResourceType() != null) {
      query.setParameter("resourceType", criteria.getResourceType().toString());
    }
    if (criteria.getResourceId() != null) {
      query.setParameter("resourceId", criteria.getResourceId());
    }
    if (criteria.getPermissions() != null) {
      query.setParameter("permissions", ResourcePermission.sum(criteria.getPermissions()));
    }
  }
  
  private String makeFindIdsMatchGrantPermissionOrderClauseSql(Sort sort) {
    String orderChain = sort.stream()
        .map(order -> order.getProperty() + " " + order.getDirection())
        .collect(Collectors.joining(", "));
  
    return new StringBuilder(" ORDER BY ").append(orderChain).toString();
  }
  
}
