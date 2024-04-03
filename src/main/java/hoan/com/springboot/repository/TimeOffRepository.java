package hoan.com.springboot.repository;

import hoan.com.springboot.common.enums.TimeOffType;
import hoan.com.springboot.models.entities.TimeOffEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * @author: Admin
 * @date: 6/27/2023
 **/
@Repository
public interface TimeOffRepository extends JpaRepository<TimeOffEntity, UUID> {
    @Query("from TimeOffEntity t where t.deleted = false and t.employeeId = :employeeId and t.type = :type")
    Optional<TimeOffEntity> findByIdAndType(UUID employeeId, TimeOffType type);
}
