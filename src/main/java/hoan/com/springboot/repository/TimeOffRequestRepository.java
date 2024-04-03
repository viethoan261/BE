package hoan.com.springboot.repository;

import hoan.com.springboot.models.entities.TimeOffRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TimeOffRequestRepository extends JpaRepository<TimeOffRequestEntity, UUID> {
    @Query("from TimeOffRequestEntity t where t.deleted = false order by t.createdAt desc")
    List<TimeOffRequestEntity> getAll();

    @Query("from TimeOffRequestEntity t where t.deleted = false and t.employeeId = :employeeId order by t.createdAt desc")
    List<TimeOffRequestEntity> myRequest(UUID employeeId);

    @Query("from TimeOffRequestEntity t where t.deleted = false and t.id = :id")
    Optional<TimeOffRequestEntity> findById(UUID id);
}
