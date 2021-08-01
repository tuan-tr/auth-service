package com.tth.auth.repository.custom;

import java.util.Collection;
import java.util.List;

import com.tth.auth.constant.ResourceType;

import org.springframework.data.domain.Sort;

public interface CustomResourceAuthorityRepository {
  
  List<String> findIdsHaveGrantPermission(Collection<String> targetIds, 
      ResourceType targetType, String specificTargetId, 
      ResourceType resourceType, String specificResourceId,
      Integer permissions, Sort sort);
  
}
