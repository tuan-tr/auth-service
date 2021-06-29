package com.tth.auth.service;

import java.util.List;
import java.util.UUID;

import com.tth.auth.configuration.security.user.UserAuthority;
import com.tth.auth.dto.group.GroupCriteria;
import com.tth.auth.dto.group.GroupDTO;
import com.tth.auth.dto.group.GroupData;
import com.tth.auth.dto.group.GroupInput;
import com.tth.auth.dto.group.GroupMemberCriteria;
import com.tth.auth.dto.group.GroupMemberData;
import com.tth.auth.dto.resourceAuthority.ResourceAccessCredential;
import com.tth.auth.dto.resourceAuthority.ResourcePermission;
import com.tth.auth.dto.resourceAuthority.ResourceType;
import com.tth.auth.entity.Group;
import com.tth.auth.entity.GroupMember;
import com.tth.auth.entity.ResourceAuthority;
import com.tth.auth.entity.User;
import com.tth.auth.exception.EntityNotFoundException;
import com.tth.auth.exception.group.DuplicateGroupMemberException;
import com.tth.auth.exception.group.GroupMemberNotFoundException;
import com.tth.auth.exception.group.GroupNotEmptyException;
import com.tth.auth.repository.GroupMemberRepository;
import com.tth.auth.repository.GroupRepository;
import com.tth.auth.repository.ResourceAuthorityRepository;
import com.tth.auth.utils.CurrentUserContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
@Transactional(readOnly = true)
public class GroupService {
  
  @Autowired
  private GroupRepository groupRepository;
  
  @Autowired
  private GroupMemberRepository groupMemberRepository;
  
  @Autowired
  private ResourceAuthorityService resourceAuthorityService;
  
  @Autowired
  private ResourceAuthorityRepository resourceAuthorityRepository;
  
  @Autowired
  private UserService userService;
  
  public Group getById(UUID id) {
    return groupRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException(Group.class.getSimpleName(), id));
  }
  
  @Transactional
  public GroupDTO create(GroupInput input) {
    Group group = Group.builder()
        .name(input.getName())
        .enabled(input.isEnabled())
        .build();
    groupRepository.save(group);
    
    UserAuthority currentUser = CurrentUserContext.get();
    ResourceAuthority currentUserAuthorityOnNewGroup = ResourceAuthority.builder()
        .targetType(ResourceType.USER)
        .targetId(currentUser.getId())
        .resourceType(ResourceType.GROUP)
        .resourceId(group.getId().toString())
        .permissions(ResourcePermission.sum(ResourcePermission.READ,
            ResourcePermission.UPDATE,
            ResourcePermission.DELETE,
            ResourcePermission.ENABLE))
        .build();
    resourceAuthorityRepository.save(currentUserAuthorityOnNewGroup);

    return GroupDTO.builder()
        .id(group.getId())
        .build();
  };

  public GroupData getDataById(UUID id) {
    GroupData group = groupRepository.findDataById(id, GroupData.class)
        .orElseThrow(() -> new EntityNotFoundException(Group.class.getSimpleName(), id));
    
    return group;
  }
  
  public Page<GroupData> getList(GroupCriteria criteria, Pageable pageable) {
    UserAuthority currentUser = CurrentUserContext.get();
    
    ResourceAccessCredential readCredential = ResourceAccessCredential.builder()
        .resourceType(ResourceType.GROUP)
        .targetIds(currentUser.getResourceAuthorities())
        .permission(ResourcePermission.READ)
        .build();
    
    boolean hasReadPermissionOnAllGroup = resourceAuthorityService
        .hasPermission(readCredential);
    
    List<UUID> readableGroupIds = null;
    if (hasReadPermissionOnAllGroup == false) {
      readableGroupIds = resourceAuthorityService.findAuthorizedResourceUUIDs(readCredential);
      if (CollectionUtils.isEmpty(readableGroupIds)) {
        return Page.empty();
      }
    }
    
    Page<GroupData> page = groupRepository.findList(readableGroupIds,
        criteria.getEnabled(),
        criteria.getKeyword(),
        pageable, GroupData.class);
    return page;
  }
  
  @Transactional
  public void update(UUID id, GroupInput input) {
    Group group = this.getById(id);
    group.setName(input.getName());
  }
  
  @Transactional
  public void enable(UUID id, boolean enabled) {
    Group group = this.getById(id);
    group.setEnabled(enabled);
  }
  
  @Transactional
  public void delete(UUID id) {
    Group group = this.getById(id);
    
    int memberCount = groupMemberRepository.countByGroupId(id);
    if (memberCount > 0) {
      throw new GroupNotEmptyException(id);
    }
    
    resourceAuthorityRepository.deleteByTargetTypeAndTargetId(ResourceType.GROUP, id);
    resourceAuthorityRepository.deleteByResourceTypeAndResourceId(ResourceType.GROUP, id.toString());
    
    groupRepository.delete(group);
  }
  
  @Transactional
  public void addMember(UUID groupId, UUID userId) {
    groupMemberRepository.findByGroupIdAndUserId(groupId, userId)
        .ifPresent(groupMember -> {
          throw new DuplicateGroupMemberException(groupId, userId);
        });
    
    Group group = this.getById(groupId);
    User user = userService.getById(userId);
    
    GroupMember groupMember = GroupMember.builder()
        .group(group)
        .user(user)
        .build();
    groupMemberRepository.save(groupMember);
  }
  
  @Transactional
  public void removeMember(UUID groupId, UUID userId) {
    GroupMember groupMember = groupMemberRepository
        .findByGroupIdAndUserId(groupId, userId)
        .orElseThrow(() -> new GroupMemberNotFoundException(groupId, userId));
    
    groupMemberRepository.delete(groupMember);
  }
  
  public Page<GroupMemberData> getMembers(UUID groupId, GroupMemberCriteria criteria, Pageable pageable) {
    return groupMemberRepository.findMembers(groupId, 
        criteria.getKeyword(),
        pageable, GroupMemberData.class);
  }
  
}
