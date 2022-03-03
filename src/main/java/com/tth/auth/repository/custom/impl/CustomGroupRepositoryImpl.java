package com.tth.auth.repository.custom.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.tth.auth.dto.group.GroupCriteria;
import com.tth.auth.entity.Group;
import com.tth.auth.entity.PersonalInformation;
import com.tth.auth.entity.User;
import com.tth.auth.repository.custom.CustomGroupRepository;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class CustomGroupRepositoryImpl implements CustomGroupRepository {
  
  @PersistenceContext
  private EntityManager entityManager;
  
  @Override
  public Page<Group> findList(Collection<String> ids, GroupCriteria criteria, Pageable pageable) {
    String whereClause = this.makeFindListWhereClauseSql(ids, criteria);
    
    StringBuilder countSqlBuilder = new StringBuilder(200)
        .append("SELECT COUNT(g.id) FROM Group g")
        .append(whereClause);
    
    TypedQuery<Long> countQuery = entityManager.createQuery(countSqlBuilder.toString(), Long.class);
    this.setFindListQueryParameter(countQuery, ids, criteria);
    Long total = countQuery.getSingleResult();
    
    if (total == 0) {
      return new PageImpl<>(Collections.emptyList(), pageable, 0);
    }
    
    String orderClause = "";
    if (pageable.getSort().isSorted()) {
      String sort = pageable.getSort().stream()
          .map(order -> order.getProperty() + " " + order.getDirection())
          .collect(Collectors.joining(", "));
    
      orderClause = new StringBuilder(" ORDER BY ").append(sort).toString();
    }
    
    StringBuilder pageSqlBuilder = new StringBuilder(200)
        .append("SELECT g FROM Group g")
        .append(whereClause)
        .append(orderClause);
    
    EntityGraph<Group> graph = entityManager.createEntityGraph(Group.class);
    graph.addSubgraph("createdBy", User.class)
        .addSubgraph("personalInformation", PersonalInformation.class);
    
    TypedQuery<Group> pageQuery = entityManager.createQuery(pageSqlBuilder.toString(), Group.class)
        .setFirstResult((int) pageable.getOffset())
        .setMaxResults(pageable.getPageSize())
        .setHint("javax.persistence.fetchgraph", graph);
    this.setFindListQueryParameter(pageQuery, ids, criteria);
    
    List<Group> entities = pageQuery.getResultList();
    return new PageImpl<>(entities, pageable, total);
  }

  private String makeFindListWhereClauseSql(Collection<String> ids, GroupCriteria criteria) {
    StringBuilder sqlBuilder = new StringBuilder(150)
        .append(" WHERE 1 = 1");
    
    if (CollectionUtils.isNotEmpty(ids)) {
      sqlBuilder.append(" AND g.id IN (:ids)");
    }
    if (StringUtils.isNoneBlank(criteria.getKeyword())) {
      sqlBuilder.append(" AND LOWER(g.name) LIKE :name");
    }
    if (criteria.getEnabled() != null) {
      sqlBuilder.append(" AND g.enabled = :enabled");
    }
    
    return sqlBuilder.toString();
  }

  private void setFindListQueryParameter(Query query, Collection<String> ids, GroupCriteria criteria) {
    if (CollectionUtils.isNotEmpty(ids)) {
      query.setParameter("ids", ids);
    }
    if (StringUtils.isNoneBlank(criteria.getKeyword())) {
      query.setParameter("name", "%" + criteria.getKeyword() + "%");
    }
    if (criteria.getEnabled() != null) {
      query.setParameter("enabled", criteria.getEnabled());
    }
  }
  
}
