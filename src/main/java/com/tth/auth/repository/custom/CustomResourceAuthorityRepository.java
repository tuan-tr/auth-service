package com.tth.auth.repository.custom;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import com.tth.auth.dto.resourceAuthority.ResourceType;

import org.springframework.data.domain.Sort;

public interface CustomResourceAuthorityRepository {
  
  List<UUID> findIdsHaveGrantPermission(Collection<UUID> targetIds, 
      ResourceType targetType, UUID specificTargetId, 
      ResourceType resourceType, String specificResourceId,
      Integer permissions, Sort sort);
  
}
