package hoan.com.springboot.repository;

import hoan.com.springboot.models.entities.DepartmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DepartmentRepository extends JpaRepository<DepartmentEntity, UUID> {

    @Query("from DepartmentEntity u where u.deleted = false and u.id = :id")
    Optional<DepartmentEntity> findById(@NotNull UUID id);

    @Query("from DepartmentEntity u where u.deleted = false order by u.name")
    List<DepartmentEntity> findAll();
}
