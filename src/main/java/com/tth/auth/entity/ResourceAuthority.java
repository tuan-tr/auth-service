package com.tth.auth.entity;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.tth.auth.dto.resourceAuthority.ResourceType;
import com.tth.auth.entity.audit.ShortAuditEntity;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "resource_authority")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class ResourceAuthority extends ShortAuditEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @Enumerated(EnumType.STRING)
  private ResourceType targetType;

  private UUID targetId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "targetId",
      referencedColumnName = "id",
      insertable = false,
      updatable = false)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "targetId",
      referencedColumnName = "id",
      insertable = false,
      updatable = false)
  private Group group;

  @Enumerated(EnumType.STRING)
  private ResourceType resourceType;

  private String resourceId;

  private int permissions;

}
