package hoan.com.springboot.services.impl;

import hoan.com.springboot.common.enums.PropertyOfRole;
import hoan.com.springboot.common.enums.RoleStatus;
import hoan.com.springboot.common.error.BadRequestError;
import hoan.com.springboot.common.exception.ResponseException;
import hoan.com.springboot.common.util.GenerateCodeHelper;
import hoan.com.springboot.mapper.RoleMapper;
import hoan.com.springboot.payload.request.RoleAssignRequest;
import hoan.com.springboot.payload.request.RoleCreateOrUpdateRequest;
import hoan.com.springboot.payload.response.RoleResponse;
import hoan.com.springboot.repository.*;
import hoan.com.springboot.services.RoleService;
import hoan.com.springboot.models.entities.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    private final RolePermissionRepository rolePermissionRepository;

    private final PermissionRepository permissionRepository;

    private final RolePropertyRepository rolePropertyRepository;

    private final UserRepository userRepository;

    private final UserRoleRepository userRoleRepository;

    @Transactional
    @Override
    public RoleEntity create(RoleCreateOrUpdateRequest request) {

        RoleEntity newRole = new RoleEntity();

        newRole.setCode(GenerateCodeHelper.generateRoleCode());
        newRole.setName(request.getName());
        newRole.setIsRoot(request.getIsRoot());
        newRole.setDescription(Objects.isNull(request.getDescription()) ? null : request.getDescription());

        roleRepository.save(newRole);

        //save property role
        List<PropertyOfRole> propertyOfRoles = request.getProperties();
        this.saveAllProperty(newRole.getId(), propertyOfRoles);

        return newRole;
    }

    @Transactional
    @Override
    public RoleEntity update(String id, RoleCreateOrUpdateRequest request) {

        Optional<RoleEntity> roleOpt = roleRepository.findById(UUID.fromString(id));

        if (roleOpt.isEmpty()) {
            throw new ResponseException(BadRequestError.INVALID_ROLE);
        }

        RoleEntity roleUpdate = roleOpt.get();

        roleUpdate.setDescription(request.getDescription());
        roleUpdate.setIsRoot(request.getIsRoot());
        roleUpdate.setName(request.getName());

        roleRepository.save(roleUpdate);

        //update propertyRole
        List<RolePropertyEntity> propertyOfRoles = rolePropertyRepository.findAllByRoleId(roleUpdate.getId());

        //delete all property role
        rolePropertyRepository.deleteAll(propertyOfRoles);

        //save role property update
        List<PropertyOfRole> properties = request.getProperties();
        this.saveAllProperty(UUID.fromString(id), properties);

        return roleUpdate;
    }

    @Transactional
    @Override
    public Boolean toggleRole(String id) {
        Optional<RoleEntity> roleOpt = roleRepository.findById(UUID.fromString(id));

        if (roleOpt.isEmpty()) {
            throw new ResponseException(BadRequestError.INVALID_ROLE);
        }

        RoleEntity roleToggle = roleOpt.get();

        if (RoleStatus.ACTIVE.equals(roleToggle.getStatus())) {
            roleToggle.setStatus(RoleStatus.INACTIVE);
        } else {
            roleToggle.setStatus(RoleStatus.ACTIVE);
        }

        roleRepository.save(roleToggle);

        return true;
    }

    @Override
    public Boolean delete(String id) {
        UUID roleId = UUID.fromString(id);

        Optional<RoleEntity> roleOpt = roleRepository.findById(roleId);

        if (roleOpt.isEmpty()) {
            throw new ResponseException(BadRequestError.INVALID_ROLE);
        }

        RoleEntity roleDelete = roleOpt.get();

        roleDelete.setDeleted(Boolean.TRUE);

        roleRepository.save(roleDelete);

        //delete role property
        List<RolePropertyEntity> rolePropertyEntities = rolePropertyRepository.findAllByRoleId(roleId);

        rolePropertyEntities.stream().forEach(t -> t.setDeleted(Boolean.TRUE));

        rolePropertyRepository.saveAll(rolePropertyEntities);

        //delete role permission
        List<RolePermissionEntity> rolePermissionEntities = rolePermissionRepository.findAllByRoleId(roleId);

        rolePermissionEntities.stream().forEach(t -> t.setDeleted(Boolean.TRUE));

        rolePermissionRepository.saveAll(rolePermissionEntities);

        return true;
    }

    /**
     * @param id
     * @param request Truyền vào một list id của permission, sau đó kiểm tra nếu id mà tồn tại thì sẽ xóa, còn chưa tồn tại thì thêm mới
     * @return
     */
    @Transactional
    @Override
    public Boolean assignPermission(String id, RoleAssignRequest request) {
        Optional<RoleEntity> roleOpt = roleRepository.findById(UUID.fromString(id));

        if (roleOpt.isEmpty()) {
            throw new ResponseException(BadRequestError.INVALID_ROLE);
        }

        //find all role permission
        List<RolePermissionEntity> rolePermissionEntities = rolePermissionRepository.findAllByRoleId(UUID.fromString(id));

        List<String> ids = rolePermissionEntities.stream().map(t -> t.getPermissionId().toString()).collect(Collectors.toList());

        List<String> permissionIds = request.getPermissionIds();

        List<String> rolePermissionAdd;
        List<RolePermissionEntity> rolePermissionRemove;

        if (!CollectionUtils.isEmpty(permissionIds)) {
            //delete old permission
            rolePermissionRemove = rolePermissionEntities.stream()
                    .filter(t -> permissionIds.contains(t.getPermissionId().toString())).collect(Collectors.toList());

            rolePermissionRepository.deleteAll(rolePermissionRemove);

            //save new permission
            rolePermissionAdd = permissionIds.stream().filter(t -> !ids.contains(t)).collect(Collectors.toList());

            this.saveAllPermission(id, rolePermissionAdd);
        }
        return true;
    }

    @Override
    public List<RoleResponse> getAll() {
        List<RoleResponse> res = new ArrayList<>();

        List<RoleEntity> roles = roleRepository.findAll();

        for (RoleEntity role : roles) {
            RoleResponse roleResponse = new RoleResponse();

            roleResponse.setName(role.getName());
            roleResponse.setCode(role.getCode());
            roleResponse.setDescription(role.getDescription());
            roleResponse.setDeleted(role.getDeleted());
            roleResponse.setStatus(role.getStatus());
            roleResponse.setIsRoot(role.getIsRoot());
            roleResponse.setCreatedAt(role.getCreatedAt());
            roleResponse.setLastModifiedAt(role.getLastModifiedAt());
            roleResponse.setCreatedBy(role.getCreatedBy());
            roleResponse.setLastModifiedBy(role.getLastModifiedBy());
            roleResponse.setId(role.getId());
            roleResponse.setProperties(rolePropertyRepository.findPropertyByRoleId(role.getId()));

            //permission
            List<RolePermissionEntity> permissions = rolePermissionRepository.findAllByRoleId(role.getId());

            if (Objects.nonNull(permissions)) {
                roleResponse.setPermissions(permissions);
            }

            res.add(roleResponse);
        }
        return res;
    }

    @Override
    public RoleResponse detail(String id) {
        RoleEntity role = this.getById(id);

        RoleResponse res = RoleMapper.INSTANCE.from(role);

        List<UserRoleEntity> userRoles = userRoleRepository.findAllByRoleIds(Arrays.asList(UUID.fromString(id)));

        if (userRoles.size() > 0) {
            List<UUID> userIds = userRoles.stream().map(t -> t.getUserId()).collect(Collectors.toList());

            List<UserEntity> users = userRepository.findByIds(userIds);

            res.setUsers(users);
        }

        res.setProperties(rolePropertyRepository.findPropertyByRoleId(role.getId()));

        //permission
        List<RolePermissionEntity> permissions = rolePermissionRepository.findAllByRoleId(role.getId());

        if (Objects.nonNull(permissions)) {
            res.setPermissions(permissions);
        }

        return res;
    }

    @Override
    public boolean addUser(String roleId, List<String> userIds) {
        RoleEntity role = this.getById(roleId);

        List<UUID> userIdsConverter = userIds.stream().map(t -> UUID.fromString(t)).collect(Collectors.toList());

        List<UserRoleEntity> userRoles = new ArrayList<>();

        for (UUID userId : userIdsConverter) {
            UserRoleEntity userRoleEntity = new UserRoleEntity();

            userRoleEntity.setUserId(userId);
            userRoleEntity.setRoleId(UUID.fromString(roleId));

            userRoles.add(userRoleEntity);
        }

        userRoleRepository.saveAll(userRoles);

        return true;
    }

    @Override
    public boolean deleteUser(String roleId, String userId) {
        RoleEntity role = this.getById(roleId);

        UserRoleEntity userRoleEntity = userRoleRepository
                .findAllByRoleIdAndUserId(UUID.fromString(roleId), UUID.fromString(userId));

        if (Objects.isNull(userRoleEntity)) {
            throw new ResponseException(BadRequestError.INVALID_ROLE);
        }

        userRoleEntity.setDeleted(Boolean.TRUE);

        userRoleRepository.save(userRoleEntity);

        return true;
    }

    private RoleEntity getById(String id) {
        Optional<RoleEntity> roleOpt = roleRepository.findById(UUID.fromString(id));

        if (roleOpt.isEmpty()) {
            throw new ResponseException(BadRequestError.INVALID_ROLE);
        }

        return roleOpt.get();
    }

    private void saveAllProperty(UUID roleId, List<PropertyOfRole> propertyOfRoles) {
        List<RolePropertyEntity> rolePropertyEntities = new ArrayList<>();
        if (!CollectionUtils.isEmpty(propertyOfRoles)) {

            for (PropertyOfRole property : propertyOfRoles) {
                RolePropertyEntity rolePropertyEntity = new RolePropertyEntity();

                rolePropertyEntity.setRoleId(roleId);
                rolePropertyEntity.setProperty(property);

                rolePropertyEntities.add(rolePropertyEntity);
            }

            rolePropertyRepository.saveAll(rolePropertyEntities);
        }
    }

    private void saveAllPermission(String roleId, List<String> rolePermissionAdd) {
        List<RolePermissionEntity> rolePermissionEntities = new ArrayList<>();

        List<UUID> ids = rolePermissionAdd.stream().map(t -> UUID.fromString(t)).collect(Collectors.toList());

        List<PermissionEntity> permissionEntities = permissionRepository.findAllByIds(ids);

        int size = permissionEntities.size();

        for (int i = 0; i < size; i++) {
            RolePermissionEntity newRoleP = new RolePermissionEntity();

            newRoleP.setRoleId(UUID.fromString(roleId));
            newRoleP.setPermissionId(permissionEntities.get(i).getId());
            newRoleP.setScope(permissionEntities.get(i).getScope());
            newRoleP.setResourceCode(permissionEntities.get(i).getResourceCode());

            rolePermissionEntities.add(newRoleP);
        }

        rolePermissionRepository.saveAll(rolePermissionEntities);
    }
}
