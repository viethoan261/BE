package hoan.com.springboot.repository;

import hoan.com.springboot.models.entities.NewsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NewsRepository extends JpaRepository<NewsEntity, UUID> {
    @Query("from NewsEntity n where n.deleted = false order by n.isImportant desc, n.createdAt desc")
    List<NewsEntity> findAll();

    @Query("from NewsEntity n where n.deleted = false and n.status = 'PUBLISHED' and n.isPublic = true or n.id in :newsIds order by n.isImportant desc, n.createdAt desc")
    List<NewsEntity> findMyNews(List<UUID> newsIds);
}
