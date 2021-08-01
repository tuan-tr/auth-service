package com.tth.auth.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.tth.auth.entity.audit.FullAuditEntity;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "_user")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class User extends FullAuditEntity {

  @Id
  @GeneratedValue(generator = "nanoid-generator", strategy = GenerationType.IDENTITY)
  @GenericGenerator(name = "nanoid-generator", strategy = "com.tth.auth.configuration.jpa.NanoidGenerator")
  private String id;

  @Column(updatable = false)
  private String username;

  private String password;

  private boolean enabled;

  @Column(updatable = false)
  private boolean rootUser;

  @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
  @JoinColumn(name = "personalInformationId", referencedColumnName = "id")
  private PersonalInformation personalInformation;

}
