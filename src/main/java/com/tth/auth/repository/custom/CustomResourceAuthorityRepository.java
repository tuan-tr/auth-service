package com.tth.auth.repository.custom;

import java.util.Collection;

import com.tth.auth.dto.resourceAuthority.ResourceAuthorityCriteria;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomResourceAuthorityRepository {
  
  Page<String> findIdsMatchGrantPermission(Collection<String> verifingTargetIds, ResourceAuthorityCriteria criteria, Pageable pageable);
  
}
