package com.tth.auth.repository;

import java.util.List;
import java.util.UUID;

import javax.validation.constraints.NotNull;

import com.tth.auth.entity.GroupMember;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.validation.annotation.Validated;

@Validated
public interface GroupMemberRepository extends JpaRepository<GroupMember, UUID> {

  @Query(nativeQuery = true, value =
    "SELECT gm.group_id "
  + "FROM {h-schema}group_member gm "
  + "LEFT JOIN {h-schema}tth_group gr ON gr.id = gm.group_id "
  + "WHERE gm.user_id = :userId "
  + "AND gr.enabled = true ")
  List<UUID> findEnabledGroupIds(@Param("userId") @NotNull UUID userId);

}
