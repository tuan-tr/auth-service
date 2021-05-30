package com.tth.auth.repository.resourceAuthorityRepositoryTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.tth.auth.dto.resourceAuthority.ResourcePermission;
import com.tth.auth.dto.resourceAuthority.ResourceType;
import com.tth.auth.entity.ResourceAuthority;
import com.tth.auth.repository.ResourceAuthorityRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Sql(scripts = "classpath:/db/resourceAuthorityTestData.sql")
public class FindMatchedTest {

  private final UUID user1Id = UUID.fromString("d1c30a05-910a-40bb-94d4-ade4337221d1");
  private final UUID user2Id = UUID.fromString("cb438448-838d-4a86-a66b-980ca5696638");

  @Autowired
  private ResourceAuthorityRepository resourceAuthorityRepository;

  @Test
  public void findMatchedOnSpecificResource_found0_readPermissionOfUser2OnUser1() {
    Pageable unpaged = Pageable.unpaged();
    List<ResourceAuthority> resourceAuthorities = resourceAuthorityRepository
        .findMatchedOnSpecificResource(
            ResourceType.USER,
            user1Id.toString(),
            Arrays.asList(user2Id),
            ResourcePermission.READ.getCode(),
            unpaged);

    assertEquals(0, resourceAuthorities.size());
  }

  @Test
  public void findMatchedOnSpecificResource_found1_readUpdatePermissionOfUser2OnUser2() {
    Pageable unpaged = Pageable.unpaged();
    List<ResourceAuthority> resourceAuthorities = resourceAuthorityRepository
        .findMatchedOnSpecificResource(
            ResourceType.USER,
            user2Id.toString(),
            Arrays.asList(user2Id),
            ResourcePermission.READ.getCode() + ResourcePermission.UPDATE.getCode(),
            unpaged);

    assertEquals(1, resourceAuthorities.size());
  }

  @Test
  public void findMatchedOnSpecificResource_found2_readPermissionOfUser1OnUser1() {
    Pageable unpaged = Pageable.unpaged();
    List<ResourceAuthority> resourceAuthorities = resourceAuthorityRepository
        .findMatchedOnSpecificResource(
            ResourceType.USER,
            user1Id.toString(),
            Arrays.asList(user1Id),
            ResourcePermission.READ.getCode(),
            unpaged);

    assertEquals(2, resourceAuthorities.size());
  }

  @Test
  public void findMatchedOnSpecificResource_found1_updateDeletePermissionOfUser1OnUser2() {
    Pageable unpaged = Pageable.unpaged();
    List<ResourceAuthority> resourceAuthorities = resourceAuthorityRepository
        .findMatchedOnSpecificResource(
            ResourceType.USER,
            user2Id.toString(),
            Arrays.asList(user1Id),
            ResourcePermission.UPDATE.getCode() + ResourcePermission.DELETE.getCode(),
            unpaged);

    assertEquals(1, resourceAuthorities.size());
  }


  @Test
  public void findMatchedOnResourceType_found0_readPermissionOfUser2OnUserType() {
    Pageable unpaged = Pageable.unpaged();
    List<ResourceAuthority> resourceAuthorities = resourceAuthorityRepository
        .findMatchedOnResourceType(
            ResourceType.USER,
            Arrays.asList(user2Id),
            ResourcePermission.READ.getCode(),
            unpaged);

    assertEquals(0, resourceAuthorities.size());
  }

  @Test
  public void findMatchedOnResourceType_found1_createReadPermissionOfUser1OnUserType() {
    Pageable unpaged = Pageable.unpaged();
    List<ResourceAuthority> resourceAuthorities = resourceAuthorityRepository
        .findMatchedOnResourceType(
            ResourceType.USER,
            Arrays.asList(user1Id),
            ResourcePermission.CREATE.getCode() + ResourcePermission.READ.getCode(),
            unpaged);

    assertEquals(1, resourceAuthorities.size());
  }

}
