package hoan.com.springboot.repository;

import hoan.com.springboot.models.entities.PermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PermissionRepository extends JpaRepository<PermissionEntity, String> {

    @Query("from PermissionEntity u where u.deleted = false and u.id in :ids")
    List<PermissionEntity> findAllByIds(List<UUID> ids);

    @Query("from PermissionEntity u where u.deleted = false order by u.priority, u.resourceCode, u.createdAt ")
    List<PermissionEntity> findAllActivated();

    @Query("from PermissionEntity u where u.deleted = false and u.id = :id")
    Optional<PermissionEntity> findById(@NotNull UUID id);

    @Query("SELECT DISTINCT p.scope FROM PermissionEntity p where p.deleted = false")
    List<String> findAllScope();

    @Query("SELECT DISTINCT p.resourceCode FROM PermissionEntity p where p.deleted = false")
    List<String> findAllResourceCode();
}
