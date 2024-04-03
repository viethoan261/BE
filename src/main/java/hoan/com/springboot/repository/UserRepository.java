package hoan.com.springboot.repository;

import hoan.com.springboot.models.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    default Optional<UserEntity> findByUsername(String username) {
        List<UserEntity> userEntities = findAllByUsername(username);
        if (!CollectionUtils.isEmpty(userEntities)) {
            return Optional.of(userEntities.get(0));
        } else {
            return Optional.empty();
        }
    }

    @Query("from UserEntity u where u.deleted = false and lower(u.username) = lower(:username)")
    List<UserEntity> findAllByUsername(String username);

    default Optional<UserEntity> findByEmployeeCode(String employeeCode) {
        List<UserEntity> userEntities = findAllByEmployeeCode(employeeCode);
        if (!CollectionUtils.isEmpty(userEntities)) {
            return Optional.of(userEntities.get(0));
        } else {
            return Optional.empty();
        }
    }

    @Query("from UserEntity u where u.deleted = false and lower(u.employeeCode) = lower(:employeeCode)")
    List<UserEntity> findAllByEmployeeCode(String employeeCode);

    default Optional<UserEntity> findByPhoneNumber(String phoneNumber) {
        List<UserEntity> userEntities = findAllByPhoneNumber(phoneNumber);
        if (!CollectionUtils.isEmpty(userEntities)) {
            return Optional.of(userEntities.get(0));
        } else {
            return Optional.empty();
        }
    }

    @Query("from UserEntity u where u.deleted = false and u.phoneNumber = :phoneNumber")
    List<UserEntity> findAllByPhoneNumber(String phoneNumber);

    default Optional<UserEntity> findByEmail(String email) {
        List<UserEntity> userEntities = findAllByEmail(email);
        if (!CollectionUtils.isEmpty(userEntities)) {
            return Optional.of(userEntities.get(0));
        } else {
            return Optional.empty();
        }
    }

    @Query("from UserEntity u where u.deleted = false and lower(u.email) = lower(:email)")
    List<UserEntity> findAllByEmail(String email);

    @Query("from UserEntity u where u.deleted = false and u.id in :ids")
    List<UserEntity> findByIds(List<UUID> ids);

    @Query("from UserEntity u where u.deleted = false order by u.fullName")
    List<UserEntity> findAll();

    @Query("from UserEntity u where u.deleted = false and u.departmentId = :departmentId order by u.fullName")
    List<UserEntity> findAllByDepartment(UUID departmentId);

//    @Query("from UserEntity u where u.deleted = false and u.status = :status and " +
//            " ( :keyword is null or ( u.username like :keyword or" +
//            " u.fullName like :keyword or u.phoneNumber like :keyword or " +
//            " u.email like :keyword))")
//    Page<UserEntity> search(@Param("keyword") String keyword, @Param("status") UserStatus status, Pageable pageable);
//
//    @Query("from UserEntity u where u.deleted = false and u.status = :status and u.organizationId = :organizationId and " +
//            " ( :keyword is null or ( u.username like :keyword or" +
//            " u.fullName like :keyword or u.phoneNumber like :keyword or " +
//            " u.email like :keyword))")
//    Page<UserEntity> search(@Param("organizationId") String organizationId, @Param("keyword") String keyword,
//                            @Param("status") UserStatus status, Pageable pageable);
//
//    @Query("from UserEntity u where u.deleted = false and u.status = :status and u.id in :userIds and " +
//            " ( :keyword is null or ( u.username like :keyword or" +
//            " u.fullName like :keyword or u.phoneNumber like :keyword or " +
//            " u.email like :keyword))")
//    Page<UserEntity> autoComplete(@Param("userIds") List<String> userIds, @Param("keyword") String keyword,
//                                  @Param("status") UserStatus status, Pageable pageable);
//
//    Long countAllByIdInAndStatus(List<String> ids, UserStatus status);
//
//    @Query("from UserEntity u where u.deleted = false")
//    List<UserEntity> findAllUserActive();

    @NotNull
    @Query("from UserEntity u where u.deleted = false and u.id = :id")
    Optional<UserEntity> findById(@NotNull UUID id);
}
