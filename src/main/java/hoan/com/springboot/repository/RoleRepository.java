package hoan.com.springboot.repository;

import hoan.com.springboot.models.entities.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, UUID> {

    @Query("from RoleEntity u where u.deleted = false and u.id in :ids")
    List<RoleEntity> findAllByIds(List<UUID> ids);

    @Query("from RoleEntity u where u.deleted = false and u.code in :roleCodes")
    List<RoleEntity> findAllByCodes(List<UUID> roleCodes);

    @Query("from RoleEntity u where u.deleted = false order by u.name")
    List<RoleEntity> findAll();

//    @Query("from RoleEntity u where u.deleted = false and u.status = :status")
//    List<RoleEntity> findAllByStatus(RoleStatus status);
//
//    @Query("select f from RoleEntity f where f.deleted = false and f.roleLevel in :roleLevels" +
//            " and (:keyword is null or (" +
//            " f.name like :keyword or" +
//            " f.code like :keyword " +
//            " ))")
//    Page<RoleEntity> search(@Param("keyword") String keyword,
//                            @Param("roleLevels") List<RoleLevel> roleLevels, Pageable pageable);
//
//    @Query("select f from RoleEntity f where f.deleted = false and f.status = :status and f.roleLevel in :roleLevels" +
//            " and (:keyword is null or (" +
//            " f.name like :keyword or" +
//            " f.code like :keyword " +
//            " ))")
//    Page<RoleEntity> search(@Param("keyword") String keyword, @Param("status") RoleStatus status,
//                            @Param("roleLevels") List<RoleLevel> roleLevels, Pageable pageable);
//
//    @Query("from RoleEntity u where u.deleted = false and u.isRoot = false and u.id in :ids and u.roleLevel in :roleLevels")
//    List<RoleEntity> findAllByIdsAAndRoleLevels(List<String> ids, List<RoleLevel> roleLevels);
//
    @NotNull
    @Query("from RoleEntity re where re.deleted = false and re.id = :id")
    Optional<RoleEntity> findById(@NotNull UUID id);
}
