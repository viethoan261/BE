package hoan.com.springboot.repository;

import hoan.com.springboot.models.entities.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRoleEntity, UUID> {

    @Query("from UserRoleEntity u where u.deleted = false and u.userId = :userId")
    List<UserRoleEntity> findAllByUserId(UUID userId);

    @Query("from UserRoleEntity ur where ur.deleted = false and ur.userId in :userIds")
    List<UserRoleEntity> findAllByUserIds(List<UUID> userIds);

    @Query("from UserRoleEntity ur where ur.deleted = false and ur.roleId in :roleIds")
    List<UserRoleEntity> findAllByRoleIds(List<UUID> roleIds);

    @Query("from UserRoleEntity ur where ur.deleted = false and ur.roleId = :roleId and ur.userId = :userId")
    UserRoleEntity findAllByRoleIdAndUserId(UUID roleId, UUID userId);
}
