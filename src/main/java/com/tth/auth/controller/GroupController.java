package com.tth.auth.controller;

import javax.validation.Valid;

import com.tth.auth.configuration.security.annotation.ResourceAuthentication;
import com.tth.auth.constant.ResourcePermission;
import com.tth.auth.constant.ResourceType;
import com.tth.auth.dto.group.GroupCriteria;
import com.tth.auth.dto.group.GroupDTO;
import com.tth.auth.dto.group.GroupDetail;
import com.tth.auth.dto.group.GroupInput;
import com.tth.auth.dto.groupMember.GroupMemberCriteria;
import com.tth.auth.dto.groupMember.GroupMemberDto;
import com.tth.auth.service.GroupService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@Validated
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(value = "/group")
public class GroupController {
  
  @Autowired
  private GroupService groupService;
  
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @ResourceAuthentication(resourceType = ResourceType.GROUP,
      permissions = ResourcePermission.CREATE)
  public GroupDTO create(@RequestBody @Valid GroupInput input) {
    return groupService.create(input);
  }
  
  @GetMapping("/{id}")
  @ResourceAuthentication(resourceType = ResourceType.GROUP,
      permissions = ResourcePermission.READ,
      resourceId = "args[0]")
  public GroupDetail getDataById(@PathVariable("id") String id) {
    return groupService.getDataById(id);
  }
  
  @GetMapping
  public Page<GroupDTO> getList(@Valid GroupCriteria criteria, Pageable pageable) {
    return groupService.getList(criteria, pageable);
  }
  
  @PutMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @ResourceAuthentication(resourceType = ResourceType.GROUP,
      permissions = ResourcePermission.UPDATE,
      resourceId = "args[0]")
  public void update(@PathVariable("id") String id,
      @RequestBody @Valid GroupInput input
  ) {
    groupService.update(id, input);
  }
  
  @PutMapping("/{id}/enabled/{enabled}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @ResourceAuthentication(resourceType = ResourceType.GROUP,
      permissions = ResourcePermission.ENABLE,
      resourceId = "args[0]")
  public void enable(@PathVariable("id") String id,
      @PathVariable("enabled") boolean enabled
  ) {
    groupService.enable(id, enabled);
  }
  
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @ResourceAuthentication(resourceType = ResourceType.GROUP,
      permissions = ResourcePermission.DELETE,
      resourceId = "args[0]")
  public void delete(@PathVariable("id") String id) {
    groupService.delete(id);
  }
  
  @PostMapping("/{groupId}/user/{userId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @ResourceAuthentication(resourceType = ResourceType.GROUP,
      permissions = ResourcePermission.ADD_ELEMENT,
      resourceId = "args[0]")
  @ResourceAuthentication(resourceType = ResourceType.USER,
      permissions = ResourcePermission.READ,
      resourceId = "args[1]")
  public void addMember(@PathVariable("groupId") String groupId,
      @PathVariable("userId") String userId
  ) {
    groupService.addMember(groupId, userId);
  }
  
  @DeleteMapping("/{groupId}/user/{userId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @ResourceAuthentication(resourceType = ResourceType.GROUP,
      permissions = ResourcePermission.ADD_ELEMENT,
      resourceId = "args[0]")
  @ResourceAuthentication(resourceType = ResourceType.USER,
      permissions = ResourcePermission.READ,
      resourceId = "args[1]")
  public void removeMember(@PathVariable("groupId") String groupId,
      @PathVariable("userId") String userId
  ) {
    groupService.removeMember(groupId, userId);
  }
  
  @GetMapping("/{id}/user")
  @ResourceAuthentication(resourceType = ResourceType.GROUP,
      permissions = ResourcePermission.READ,
      resourceId = "args[0]")
  public Page<GroupMemberDto> getMembers(@PathVariable("id") String groupId,
      @Valid GroupMemberCriteria criteria, Pageable pageable
  ) {
    return groupService.getMembers(groupId, criteria, pageable);
  }
  
}
