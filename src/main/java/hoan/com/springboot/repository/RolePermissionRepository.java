package hoan.com.springboot.repository;

import hoan.com.springboot.models.entities.RolePermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermissionEntity, UUID> {

    @Query("from RolePermissionEntity u where u.deleted = false and u.id in :ids")
    List<RolePermissionEntity> findAllByIds(List<UUID> ids);

    @Query("from RolePermissionEntity u where u.deleted = false and u.roleId = :roleId")
    List<RolePermissionEntity> findAllByRoleId(UUID roleId);

    @Query("from RolePermissionEntity u where u.deleted = false and u.roleId in :roleIds")
    List<RolePermissionEntity> findAllByRoleIds(List<UUID> roleIds);
//
//    @Query("from RolePermissionEntity u where u.deleted = false")
//    List<RolePermissionEntity> findAllActivated();
//
//    @Query("from RolePermissionEntity u where u.deleted = false and u.resourceCode = :resourceCode and u.scope = :scope")
//    List<RolePermissionEntity> findRolePermissionEntityByResourceCodeAndScope(String resourceCode, Scope scope);
}
