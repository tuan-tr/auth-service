package com.tth.auth.entity;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.tth.auth.configuration.hibernate.NanoidGenerator;
import com.tth.auth.constant.ResourceType;
import com.tth.auth.entity.audit.ModifyAuditEntity;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JoinFormula;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class ResourceAuthority extends ModifyAuditEntity {

  @Id
  @GeneratedValue(generator = "nanoid-generator", strategy = GenerationType.IDENTITY)
  @GenericGenerator(name = "nanoid-generator", strategy = NanoidGenerator.NAME)
  private String id;

  @Enumerated(EnumType.STRING)
  private ResourceType targetType;

  private String targetId;

  @ManyToOne(fetch = FetchType.LAZY)
  // @JoinColumn(name = "targetId", referencedColumnName = "id", 
  //     insertable = false, updatable = false)
  @JoinFormula(value = "(SELECT target_id WHERE target_type = 'USER')", 
      referencedColumnName = "id")
  private User targetUser;

  @ManyToOne(fetch = FetchType.LAZY)
  // @JoinColumn(name = "targetId", referencedColumnName = "id",
  //     insertable = false, updatable = false)
  @JoinFormula(value = "(SELECT target_id WHERE target_type = 'GROUP')", 
      referencedColumnName = "id")
  private Group targetGroup;

  @Enumerated(EnumType.STRING)
  private ResourceType resourceType;

  private String resourceId;
  
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinFormula(value = "(SELECT resource_id WHERE resource_type = 'USER')", 
      referencedColumnName = "id")
  private User resourceUser;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinFormula(value = "(SELECT resource_id WHERE resource_type = 'GROUP')", 
      referencedColumnName = "id")
  private Group resourceGroup;

  private int permissions;

}
