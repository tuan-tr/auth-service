package com.tth.auth.repository.custom.impl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.tth.auth.dto.groupMember.GroupMemberCriteria;
import com.tth.auth.entity.GroupMember;
import com.tth.auth.repository.custom.CustomGroupMemberRepository;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class CustomGroupMemberRepositoryImpl implements CustomGroupMemberRepository {
  
  @PersistenceContext
  private EntityManager entityManager;
  
  @Override
  public Page<GroupMember> findList(String groupId, GroupMemberCriteria criteria, Pageable pageable) {
    String whereClause = this.makeFindListWhereClauseSql(groupId, criteria);
    
    StringBuilder countSqlBuilder = new StringBuilder(300)
        .append("SELECT COUNT(gm.id) FROM GroupMember gm")
        .append(" LEFT JOIN gm.user u")
        .append(" LEFT JOIN u.personalInformation upi")
        .append(whereClause);
    
    TypedQuery<Long> countQuery = entityManager.createQuery(countSqlBuilder.toString(), Long.class);
    this.setFindListQueryParameter(countQuery, groupId, criteria);
    Long total = countQuery.getSingleResult();
    
    if (total == 0) {
      return new PageImpl<>(Collections.emptyList(), pageable, 0);
    }
    
    String orderClause = "";
    if (pageable.getSort().isSorted()) {
      String sort = pageable.getSort().stream()
          .map(order -> order.getProperty() + " " + order.getDirection())
          .collect(Collectors.joining(", "));
    
      orderClause = " ORDER BY " + sort;
    }
    
    StringBuilder pageSqlBuilder = new StringBuilder(300)
        .append("SELECT gm FROM GroupMember gm")
        .append(" LEFT JOIN FETCH gm.user u")
        .append(" LEFT JOIN FETCH u.personalInformation upi")
        .append(" LEFT JOIN FETCH gm.modifiedBy mb")
        .append(" LEFT JOIN FETCH mb.personalInformation mbpi")
        .append(whereClause)
        .append(orderClause);
    
    TypedQuery<GroupMember> pageQuery = entityManager.createQuery(pageSqlBuilder.toString(), GroupMember.class)
        .setFirstResult((int) pageable.getOffset())
        .setMaxResults(pageable.getPageSize());
    this.setFindListQueryParameter(pageQuery, groupId, criteria);
    
    List<GroupMember> entities = pageQuery.getResultList();
    return new PageImpl<>(entities, pageable, total);
  }

  private String makeFindListWhereClauseSql(String groupId, GroupMemberCriteria criteria) {
    StringBuilder sqlBuilder = new StringBuilder(200)
        .append(" WHERE 1=1");
    
    if (StringUtils.isNotBlank(groupId)) {
      sqlBuilder.append(" AND gm.groupId = :groupId");
    }
    if (StringUtils.isNotBlank(criteria.getKeyword())) {
      sqlBuilder.append(" AND( LOWER(u.username) LIKE :keyword")
          .append(" OR CONCAT(upi.lastName, ' ', upi.firstName) LIKE :keyword )");
    }
    
    return sqlBuilder.toString();
  }

  private void setFindListQueryParameter(Query query, String groupId, GroupMemberCriteria criteria) {
    if (StringUtils.isNotBlank(groupId)) {
      query.setParameter("groupId", groupId);
    }
    if (StringUtils.isNotBlank(criteria.getKeyword())) {
      query.setParameter("keyword", "%" + criteria.getKeyword() + "%");
    }
  }
  
}
