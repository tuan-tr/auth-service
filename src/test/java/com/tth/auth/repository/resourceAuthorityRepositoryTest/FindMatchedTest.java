package com.tth.auth.repository.resourceAuthorityRepositoryTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import com.tth.auth.configuration.HibernateConfigurer;
import com.tth.auth.configuration.hibernate.AuditorAwareImpl;
import com.tth.auth.configuration.hibernate.DateTimeProviderImpl;
import com.tth.auth.constant.ResourcePermission;
import com.tth.auth.constant.ResourceType;
import com.tth.auth.entity.ResourceAuthority;
import com.tth.auth.repository.ResourceAuthorityRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Sql(scripts = "classpath:/db/resourceAuthorityTestData.sql")
@Import({ HibernateConfigurer.class, AuditorAwareImpl.class, DateTimeProviderImpl.class })
public class FindMatchedTest {

  private final String user1Id = "user1Id";
  private final String user2Id = "user2Id";

  @Autowired
  private ResourceAuthorityRepository resourceAuthorityRepository;

  @Test
  public void findOnSpecificResource_found0_readPermissionOfUser2OnUser1() {
    Pageable unpaged = Pageable.unpaged();
    List<ResourceAuthority> resourceAuthorities = resourceAuthorityRepository
        .findOnSpecificResource(
            ResourceType.USER,
            user1Id,
            List.of(user2Id),
            ResourcePermission.READ.getCode(),
            unpaged);

    assertEquals(0, resourceAuthorities.size());
  }

  @Test
  public void findOnSpecificResource_found1_readUpdatePermissionOfUser2OnUser2() {
    Pageable unpaged = Pageable.unpaged();
    List<ResourceAuthority> resourceAuthorities = resourceAuthorityRepository
        .findOnSpecificResource(
            ResourceType.USER,
            user2Id,
            List.of(user2Id),
            ResourcePermission.READ.getCode() + ResourcePermission.UPDATE.getCode(),
            unpaged);

    assertEquals(1, resourceAuthorities.size());
  }

  @Test
  public void findOnSpecificResource_found2_readPermissionOfUser1OnUser1() {
    Pageable unpaged = Pageable.unpaged();
    List<ResourceAuthority> resourceAuthorities = resourceAuthorityRepository
        .findOnSpecificResource(
            ResourceType.USER,
            user1Id,
            List.of(user1Id),
            ResourcePermission.READ.getCode(),
            unpaged);

    assertEquals(2, resourceAuthorities.size());
  }

  @Test
  public void findOnSpecificResource_found1_updateDeletePermissionOfUser1OnUser2() {
    Pageable unpaged = Pageable.unpaged();
    List<ResourceAuthority> resourceAuthorities = resourceAuthorityRepository
        .findOnSpecificResource(
            ResourceType.USER,
            user2Id,
            List.of(user1Id),
            ResourcePermission.UPDATE.getCode() + ResourcePermission.DELETE.getCode(),
            unpaged);

    assertEquals(1, resourceAuthorities.size());
  }


  @Test
  public void findOnResourceType_found0_readPermissionOfUser2OnUserType() {
    Pageable unpaged = Pageable.unpaged();
    List<ResourceAuthority> resourceAuthorities = resourceAuthorityRepository
        .findOnResourceType(
            ResourceType.USER,
            List.of(user2Id),
            ResourcePermission.READ.getCode(),
            unpaged);

    assertEquals(0, resourceAuthorities.size());
  }

  @Test
  public void findOnResourceType_found1_createReadPermissionOfUser1OnUserType() {
    Pageable unpaged = Pageable.unpaged();
    List<ResourceAuthority> resourceAuthorities = resourceAuthorityRepository
        .findOnResourceType(
            ResourceType.USER,
            List.of(user1Id),
            ResourcePermission.CREATE.getCode() + ResourcePermission.READ.getCode(),
            unpaged);

    assertEquals(1, resourceAuthorities.size());
  }

}
