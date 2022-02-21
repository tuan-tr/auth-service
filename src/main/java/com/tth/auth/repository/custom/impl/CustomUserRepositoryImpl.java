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

import com.tth.auth.dto.personalInformation.PersonalInformationDTO;
import com.tth.auth.dto.user.UserCriteria;
import com.tth.auth.dto.user.UserDTO;
import com.tth.auth.entity.PersonalInformation;
import com.tth.auth.entity.User;
import com.tth.auth.repository.custom.CustomUserRepository;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class CustomUserRepositoryImpl implements CustomUserRepository {
  
  @PersistenceContext
  private EntityManager entityManager;
  
  @Override
  public Page<UserDTO> findList(Collection<String> ids, UserCriteria criteria, Pageable pageable) {
    String whereClause = this.makeFindListWhereClauseSql(ids, criteria);
    
    StringBuilder countSqlBuilder = new StringBuilder(300)
        .append("SELECT COUNT(u.id) FROM User u")
        .append(" LEFT JOIN u.personalInformation pi")
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
    
    StringBuilder pageSqlBuilder = new StringBuilder(300)
        .append("SELECT u FROM User u")
        .append(" LEFT JOIN FETCH u.personalInformation pi")
        .append(whereClause)
        .append(orderClause);
    
    EntityGraph<User> graph = entityManager.createEntityGraph(User.class);
    graph.addSubgraph("personalInformation", PersonalInformation.class);
    
    TypedQuery<User> pageQuery = entityManager.createQuery(pageSqlBuilder.toString(), User.class)
        .setFirstResult((int) pageable.getOffset())
        .setMaxResults(pageable.getPageSize());
    this.setFindListQueryParameter(pageQuery, ids, criteria);
    
    List<User> entities = pageQuery.getResultList();
    List<UserDTO> content = this.convertToDTO(entities);
    
    return new PageImpl<>(content, pageable, total);
  }

  private String makeFindListWhereClauseSql(Collection<String> ids, UserCriteria criteria) {
    StringBuilder sqlBuilder = new StringBuilder(250)
        .append(" WHERE 1=1");
    
    if (CollectionUtils.isNotEmpty(ids)) {
      sqlBuilder.append(" AND u.id IN (:ids)");
    }
    if (criteria.getEnabled() != null) {
      sqlBuilder.append(" AND u.enabled = :enabled");
    }
    if (StringUtils.isNoneBlank(criteria.getKeyword())) {
      sqlBuilder.append(" AND( LOWER(u.username) LIKE :keyword")
          .append(" OR CONCAT(pi.lastName, ' ', pi.firstName) LIKE :keyword )");
    }
    if (criteria.getGender() != null) {
      sqlBuilder.append(" AND pi.gender = :gender");
    }
    
    return sqlBuilder.toString();
  }

  private void setFindListQueryParameter(Query query, Collection<String> ids, UserCriteria criteria) {
    if (CollectionUtils.isNotEmpty(ids)) {
      query.setParameter("ids", ids);
    }
    if (criteria.getEnabled() != null) {
      query.setParameter("enabled", criteria.getEnabled());
    }
    if (StringUtils.isNoneBlank(criteria.getKeyword())) {
      query.setParameter("keyword", "%" + criteria.getKeyword() + "%");
    }
    if (criteria.getGender() != null) {
      query.setParameter("gender", criteria.getEnabled());
    }
  }
  
  private List<UserDTO> convertToDTO(List<User> entities) {
    return entities.stream()
        .map(entity -> this.convertToDTO(entity))
        .collect(Collectors.toList());
  }
  
  private UserDTO convertToDTO(User entity) {
    PersonalInformation infor = entity.getPersonalInformation();
    return UserDTO.builder()
        .id(entity.getId())
        .username(entity.getUsername())
        .enabled(entity.isEnabled())
        .personalInformation(infor == null ? null : PersonalInformationDTO.builder()
            .firstName(infor.getFirstName())
            .lastName(infor.getLastName())
            .birthdate(infor.getBirthdate())
            .gender(infor.getGender())
            .build())
        .build();
  }
  
}
