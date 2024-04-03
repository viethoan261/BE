package hoan.com.springboot.repository;

import hoan.com.springboot.models.entities.RolePropertyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RolePropertyRepository extends JpaRepository<RolePropertyEntity, UUID> {

    @Query("from RolePropertyEntity  rp where rp.roleId = :roleId and rp.deleted = false")
    List<RolePropertyEntity> findAllByRoleId(UUID roleId);

    @Query("select rp.property from RolePropertyEntity  rp where rp.roleId = :roleId and rp.deleted = false")
    List<String> findPropertyByRoleId(UUID roleId);
}
