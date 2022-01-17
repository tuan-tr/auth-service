package com.tth.auth.repository.custom;

import java.util.Collection;

import com.tth.auth.dto.group.GroupCriteria;
import com.tth.auth.dto.group.GroupDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomGroupRepository {
  
  Page<GroupDTO> findList(Collection<String> ids, GroupCriteria criteria, Pageable pageable);
  
}
