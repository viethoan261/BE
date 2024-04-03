package hoan.com.springboot.repository;

import hoan.com.springboot.models.entities.AttendanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AttendanceRepository extends JpaRepository<AttendanceEntity, UUID> {
    @Query(value = "SELECT * FROM `attendance` a where date(a.start) = CURRENT_DATE() and a.employee_id = :id", nativeQuery = true)
    Optional<AttendanceEntity> findByEmployeeIdAndDate(String id);

    @Query("from AttendanceEntity a order by a.employeeName, a.start")
    List<AttendanceEntity> findAll();

    @Query("from AttendanceEntity a where a.employeeId = :employeeId order by a.start")
    List<AttendanceEntity> getMyAttendance(UUID employeeId);
}
