package com.tth.auth.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.tth.auth.configuration.hibernate.NanoidGenerator;
import com.tth.auth.entity.audit.CreateModifyAuditEntity;

import org.hibernate.annotations.GenericGenerator;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "_group")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Group extends CreateModifyAuditEntity {

  @Id
  @GeneratedValue(generator = "nanoid-generator", strategy = GenerationType.IDENTITY)
  @GenericGenerator(name = "nanoid-generator", strategy = NanoidGenerator.NAME)
  private String id;

  private String name;

  private boolean enabled;

}
