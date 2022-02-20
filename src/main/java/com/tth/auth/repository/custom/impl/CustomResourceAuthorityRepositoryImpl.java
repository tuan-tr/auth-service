package com.tth.auth.repository.custom.impl;

import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.tth.auth.constant.ResourcePermission;
import com.tth.auth.constant.ResourceType;
import com.tth.auth.repository.custom.CustomResourceAuthorityRepository;

import org.springframework.data.domain.Sort;

public class CustomResourceAuthorityRepositoryImpl implements CustomResourceAuthorityRepository {
  
  @PersistenceContext
  private EntityManager entityManager;
  
  @Override
  public List<String> findIdsHaveGrantPermission(Collection<String> targetIds, 
      ResourceType targetType, String specificTargetId, 
      ResourceType resourceType, String specificResourceId,
      Integer permissions, Sort sort
  ) {
    int grantCode = ResourcePermission.GRANT_PERMISSION.getCode();
    List<String> allTargetType = List.of(ResourceType.USER.toString(), ResourceType.GROUP.toString());
    
    StringBuilder sqlBuilder = new StringBuilder()
    .append("SELECT ")
    .append("  ra.id AS id ")
    // .append(" ,ra.target_type AS \"targetType\" ")
    // .append(" ,ra.resource_type AS \"resourceType\" ")
    .append("FROM {h-schema}resource_authority ra ");
    
    if (specificTargetId == null) {
      sqlBuilder
      .append("RIGHT JOIN ( ")
      .append("  SELECT DISTINCT ratg.resource_id, ratg.resource_type ")
      .append("  FROM {h-schema}resource_authority ratg ")
      .append("  WHERE ratg.permissions & :grantCode = :grantCode ");
      if (targetType != null) {
        sqlBuilder.append("  AND ratg.resource_type = :targetType ");
      } else {
        sqlBuilder.append("  AND ratg.resource_type IN (:allTargetType) ");
      }
      sqlBuilder
      .append("  AND ratg.target_id IN (:targetIds) ")
      .append(") AS ratg ON ratg.resource_type = ra.target_type ")
      .append("  AND (ratg.resource_id IS NULL OR ratg.resource_id = CAST(ra.target_id AS VARCHAR)) ");
    }
    if (specificResourceId == null) {
      sqlBuilder
      .append("RIGHT JOIN ( ")
      .append("  SELECT DISTINCT rars.resource_id, rars.resource_type ")
      .append("  FROM {h-schema}resource_authority rars ")
      .append("  WHERE rars.permissions & :grantCode = :grantCode ");
      if (resourceType != null) {
        sqlBuilder.append("  AND rars.resource_type = :resourceType ");
      }
      sqlBuilder
      .append("  AND rars.target_id IN (:targetIds) ")
      .append(") AS rars ON rars.resource_type = ra.resource_type ")
      .append("  AND (rars.resource_id IS NULL OR rars.resource_id = ra.resource_id) ");
    }
    
    sqlBuilder.append("WHERE 1 = 1 ");
    if (specificTargetId != null) {
      sqlBuilder
      .append("  AND ra.target_type = :targetType ")
      .append("  AND ra.target_id = :specificTargetId ");
    }
    if (specificResourceId != null) {
      sqlBuilder
      .append("  AND ra.resource_type = :resourceType ")
      .append("  AND ra.resource_id = :specificResourceId ");
    }
    
    Query query = entityManager.createNativeQuery(sqlBuilder.toString());
    if (specificTargetId == null) {
      query.setParameter("grantCode", grantCode);
      query.setParameter("targetIds", targetIds);
      if (targetType != null) {
        query.setParameter("targetType", targetType.toString());
      } else {
        query.setParameter("allTargetType", allTargetType);
      }
    } else {
      query.setParameter("targetType", targetType.toString());
      query.setParameter("specificTargetId", specificTargetId);
    }
    
    if (specificResourceId == null) {
      query.setParameter("grantCode", grantCode);
      query.setParameter("targetIds", targetIds);
      if (resourceType != null) {
        query.setParameter("resourceType", resourceType.toString());
      }
    } else {
      query.setParameter("resourceType", resourceType.toString());
      query.setParameter("specificResourceId", specificResourceId);
    }
    
    // StandardBasicTypeTemplate<ResourceType> resourceTypeCustom = new StandardBasicTypeTemplate<>(
    //     VarcharTypeDescriptor.INSTANCE, new EnumJavaTypeDescriptor<>(ResourceType.class));
    
    List<String> rs = query.getResultList();
        // .unwrap(org.hibernate.query.NativeQuery.class)
        // .addScalar("targetType", resourceTypeCustom)
        // .addScalar("resourceType", resourceTypeCustom)
        // .setResultTransformer(Transformers.aliasToBean(ResourceAuthorityDTO.class))
    
    return rs;
  }
  
}
