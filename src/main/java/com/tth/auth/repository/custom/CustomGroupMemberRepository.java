package com.tth.auth.repository.custom;

import com.tth.auth.dto.groupMember.GroupMemberCriteria;
import com.tth.auth.entity.GroupMember;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomGroupMemberRepository {
  
  Page<GroupMember> findList(String groupId, GroupMemberCriteria criteria, Pageable pageable);
  
}
