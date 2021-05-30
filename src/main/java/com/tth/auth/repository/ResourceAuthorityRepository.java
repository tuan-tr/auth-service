package com.tth.auth.repository;

import java.util.UUID;

import com.tth.auth.entity.ResourceAuthority;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.validation.annotation.Validated;

@Validated
public interface ResourceAuthorityRepository extends JpaRepository<ResourceAuthority, UUID> {

}
