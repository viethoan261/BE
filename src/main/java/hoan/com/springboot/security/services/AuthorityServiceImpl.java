package hoan.com.springboot.security.services;

import hoan.com.springboot.common.UserAuthority;
import hoan.com.springboot.common.enums.UserStatus;
import hoan.com.springboot.common.error.AuthorizationError;
import hoan.com.springboot.common.error.BadRequestError;
import hoan.com.springboot.common.error.NotFoundError;
import hoan.com.springboot.common.exception.ResponseException;
import hoan.com.springboot.models.entities.RoleEntity;
import hoan.com.springboot.models.entities.RolePermissionEntity;
import hoan.com.springboot.models.entities.UserEntity;
import hoan.com.springboot.models.entities.UserRoleEntity;
import hoan.com.springboot.repository.RolePermissionRepository;
import hoan.com.springboot.repository.RoleRepository;
import hoan.com.springboot.repository.UserRepository;
import hoan.com.springboot.repository.UserRoleRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Primary
@Slf4j
@AllArgsConstructor
public class AuthorityServiceImpl implements AuthorityService {

    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final UserRepository userRepository;

    @Override
    public UserAuthority getUserAuthority(String userId) {

        // lấy user theo id, id này được lấy khi đăng nhập
        UserEntity userEntity = ensureUserExisted(UUID.fromString(userId));

        // kiểm tra trạng thái của user, nếu inactive thì sẽ bắn ra lỗi
        if (UserStatus.INACTIVE.equals(userEntity.getStatus())) {
            throw new ResponseException(BadRequestError.LOGIN_FAIL_DUE_INACTIVE_ACCOUNT);
        }

        List<String> grantedAuthorities = new ArrayList<>();

        boolean isRoot = false;

        //lấy ra danh sách role của user đó
        List<UserRoleEntity> userRoleEntities = this.userRoleRepository.findAllByUserId(userEntity.getId());

        if (!CollectionUtils.isEmpty(userRoleEntities)) {
//            List<String> roleIds = userRoleEntities.stream()
//                    .map(t -> t.getRoleId().toString()).distinct().collect(Collectors.toList());
            // lấy ra list id của các role
            List<UUID> ids = userRoleEntities.stream()
                    .map(t -> t.getRoleId()).distinct().collect(Collectors.toList());
            List<RoleEntity> roleEntities = this.roleRepository.findAllByIds(ids);
//            roleIds = roleEntities.stream()
//                    .filter(r -> Objects.equals(RoleStatus.ACTIVE, r.getStatus()))
//                    .map(t -> t.getId().toString()).distinct().collect(Collectors.toList());

            //kiểm tra xem tồn tại 1 role trong list role có isRoot = true ( tức là quyền quản trị hệ thống)
            isRoot = roleEntities.stream().anyMatch(r -> Boolean.TRUE.equals(r.getIsRoot()));

            //lấy ra các permission của các role
            List<RolePermissionEntity> rolePermissionEntities = this.rolePermissionRepository.findAllByRoleIds(ids);

            if (!CollectionUtils.isEmpty(rolePermissionEntities)) {
                grantedAuthorities = rolePermissionEntities.stream()
                        .map(r -> String.format("%s:%s", r.getResourceCode().toString().toLowerCase(), r.getScope().toString().toLowerCase()))
                        .distinct().collect(Collectors.toList());
            }
        } else {
            log.info("User {} don't has role", userId);
        }

        return UserAuthority.builder()
                .isRoot(isRoot)
                .grantedPermissions(grantedAuthorities)
                .userId(userEntity.getId().toString())
                .build();
    }

    private UserEntity ensureUserExisted(UUID userId) {
        return this.userRepository.findById(userId).orElseThrow(() ->
                new ResponseException(NotFoundError.USER_NOT_FOUND));
    }

    private List<String> getGrantedAuthorityByRoleId(String roleId) {
        List<String> grantedAuthorities = new ArrayList<>();
        Optional<RoleEntity> roleEntity = this.roleRepository.findById(UUID.fromString(roleId));
        if (!roleEntity.isPresent()) {
            return grantedAuthorities;
        }
        RoleEntity role = roleEntity.get();
        List<UUID> roleIds = Collections.singletonList(role.getId());
        List<RolePermissionEntity> rolePermissionEntities = this.rolePermissionRepository.findAllByRoleIds(roleIds);

        if (!CollectionUtils.isEmpty(rolePermissionEntities)) {
            grantedAuthorities = rolePermissionEntities.stream()
                    .map(r -> String.format("%s:%s", r.getResourceCode().toString().toLowerCase(), r.getScope().toString().toLowerCase()))
                    .distinct().collect(Collectors.toList());
        }
        return grantedAuthorities;
    }

    Boolean checkRoleAdminOfUser(String userId) {
        List<UserRoleEntity> userRoleEntities = userRoleRepository.findAllByUserId(UUID.fromString(userId));
        if (CollectionUtils.isEmpty(userRoleEntities)) {
            throw new ResponseException(AuthorizationError.USER_DO_NOT_HAVE_ROLE);
        } else {
            List<String> roleIds = userRoleEntities.stream().map(t -> t.getRoleId().toString()).collect(Collectors.toList());
            List<UUID> ids = userRoleEntities.stream()
                    .map(t -> t.getRoleId()).collect(Collectors.toList());
            List<RoleEntity> roleEntities = roleRepository.findAllByIds(ids);
            Optional<RoleEntity> roleAdmin = roleEntities.stream().filter(RoleEntity::getIsRoot).findFirst();
            return roleAdmin.isPresent();
        }
    }
}