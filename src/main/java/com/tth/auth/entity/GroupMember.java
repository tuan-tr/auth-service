package com.tth.auth.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.tth.auth.entity.audit.ShortAuditEntity;

import org.hibernate.annotations.GenericGenerator;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GroupMember extends ShortAuditEntity {

  @Id
  @GeneratedValue(generator = "nanoid-generator", strategy = GenerationType.IDENTITY)
  @GenericGenerator(name = "nanoid-generator", strategy = "com.tth.auth.configuration.jpa.NanoidGenerator")
  private String id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "userId", referencedColumnName = "id")
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "groupId", referencedColumnName = "id")
  private Group group;
  
  @Column(insertable = false, updatable = false)
  private String groupId;

}
