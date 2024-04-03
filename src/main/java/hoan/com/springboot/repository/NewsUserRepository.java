package hoan.com.springboot.repository;

import hoan.com.springboot.models.entities.NewsUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface NewsUserRepository extends JpaRepository<NewsUserEntity, UUID> {
    @Query("from NewsUserEntity n where n.employeeId = :employeeId")
    List<NewsUserEntity> findByEmployeeId(UUID employeeId);

    @Query("from NewsUserEntity n where n.newsId = :newsId")
    List<NewsUserEntity> findByNewsId(UUID newsId);
}
