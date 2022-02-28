package com.tth.auth.repository;

import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotBlank;

import com.tth.auth.entity.GroupMember;
import com.tth.auth.repository.custom.CustomGroupMemberRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.validation.annotation.Validated;

@Validated
public interface GroupMemberRepository extends JpaRepository<GroupMember, String>, CustomGroupMemberRepository {
  
  int countByGroupId(String groupId);
  
  Optional<GroupMember> findByGroupIdAndUserId(@NotBlank String groupId, @NotBlank String userId);
  
  @Query(nativeQuery = true, value =
    "SELECT gm.group_id "
  + "FROM {h-schema}group_member gm "
  + "LEFT JOIN {h-schema}_group gr ON gr.id = gm.group_id "
  + "WHERE gm.user_id = :userId "
  + "AND gr.enabled = true ")
  List<String> findEnabledGroupIds(@Param("userId") @NotBlank String userId);
  
}
