package com.tth.auth.service;

import java.util.List;

import com.tth.auth.configuration.security.user.UserAuthority;
import com.tth.auth.constant.ResourcePermission;
import com.tth.auth.constant.ResourceType;
import com.tth.auth.dto.group.GroupCriteria;
import com.tth.auth.dto.group.GroupDTO;
import com.tth.auth.dto.group.GroupDetail;
import com.tth.auth.dto.group.GroupInput;
import com.tth.auth.dto.groupMember.GroupMemberCriteria;
import com.tth.auth.dto.groupMember.GroupMemberDto;
import com.tth.auth.dto.resourceAuthority.ResourceAccessCredential;
import com.tth.auth.entity.Group;
import com.tth.auth.entity.GroupMember;
import com.tth.auth.entity.ResourceAuthority;
import com.tth.auth.entity.User;
import com.tth.auth.exception.EntityNotFoundException;
import com.tth.auth.exception.group.DuplicateGroupMemberException;
import com.tth.auth.exception.group.GroupMemberNotFoundException;
import com.tth.auth.exception.group.GroupNotEmptyException;
import com.tth.auth.projector.GroupMemberProjector;
import com.tth.auth.repository.GroupMemberRepository;
import com.tth.auth.repository.GroupRepository;
import com.tth.auth.repository.ResourceAuthorityRepository;
import com.tth.auth.util.CurrentUserContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
  
  public Group getById(String id) {
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
        .resourceId(group.getId())
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

  public GroupDetail getDataById(String id) {
    GroupDetail group = groupRepository.findDataById(id, GroupDetail.class)
        .orElseThrow(() -> new EntityNotFoundException(Group.class.getSimpleName(), id));
    
    return group;
  }
  
  public Page<GroupDTO> getList(GroupCriteria criteria, Pageable pageable) {
    UserAuthority currentUser = CurrentUserContext.get();
    
    ResourceAccessCredential readCredential = ResourceAccessCredential.builder()
        .resourceType(ResourceType.GROUP)
        .targetIds(currentUser.getResourceAuthorities())
        .permission(ResourcePermission.READ)
        .build();
    
    boolean hasReadPermissionOnAllGroup = resourceAuthorityService
        .hasPermission(readCredential);
    
    List<String> readableGroupIds = null;
    if (hasReadPermissionOnAllGroup == false) {
      readableGroupIds = resourceAuthorityService.getAuthorizedResourceIds(readCredential);
      if (CollectionUtils.isEmpty(readableGroupIds)) {
        return Page.empty();
      }
    }
    
    Page<GroupDTO> page = groupRepository.findList(readableGroupIds,
        criteria, pageable);
    return page;
  }
  
  @Transactional
  public void update(String id, GroupInput input) {
    Group group = this.getById(id);
    group.setName(input.getName());
  }
  
  @Transactional
  public void enable(String id, boolean enabled) {
    Group group = this.getById(id);
    group.setEnabled(enabled);
  }
  
  @Transactional
  public void delete(String id) {
    Group group = this.getById(id);
    
    int memberCount = groupMemberRepository.countByGroupId(id);
    if (memberCount > 0) {
      throw new GroupNotEmptyException(id);
    }
    
    resourceAuthorityRepository.deleteByTargetTypeAndTargetId(ResourceType.GROUP, id);
    resourceAuthorityRepository.deleteByResourceTypeAndResourceId(ResourceType.GROUP, id);
    
    groupRepository.delete(group);
  }
  
  @Transactional
  public void addMember(String groupId, String userId) {
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
  public void removeMember(String groupId, String userId) {
    GroupMember groupMember = groupMemberRepository
        .findByGroupIdAndUserId(groupId, userId)
        .orElseThrow(() -> new GroupMemberNotFoundException(groupId, userId));
    
    groupMemberRepository.delete(groupMember);
  }
  
  public Page<GroupMemberDto> getMembers(String groupId, GroupMemberCriteria criteria, Pageable pageable) {
    Page<GroupMember> page = groupMemberRepository.findList(groupId, criteria, pageable);
    List<GroupMemberDto> content = GroupMemberProjector.convertToDto(page.getContent());
    return new PageImpl<>(content, pageable, page.getTotalElements());
  }
  
}
