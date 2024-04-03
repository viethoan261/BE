package hoan.com.springboot.repository;

import hoan.com.springboot.models.entities.TimeOffHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author: Admin
 * @date: 6/27/2023
 **/
@Repository
public interface TimeOffHistoryRepository extends JpaRepository<TimeOffHistoryEntity, UUID> {
    @Query("from TimeOffHistoryEntity t where t.deleted = false and t.employeeId = :employeeId order by t.date desc")
    List<TimeOffHistoryEntity> findAllByEmployeeId(UUID employeeId);

    @Query("from TimeOffHistoryEntity t where t.deleted = false and t.requestId = :requestId")
    Optional<TimeOffHistoryEntity> findByRequestId(UUID requestId);
}
