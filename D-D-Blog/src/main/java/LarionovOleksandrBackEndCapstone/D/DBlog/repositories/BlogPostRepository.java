package LarionovOleksandrBackEndCapstone.D.DBlog.repositories;

import LarionovOleksandrBackEndCapstone.D.DBlog.entities.BlogPost;
import LarionovOleksandrBackEndCapstone.D.DBlog.entities.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BlogPostRepository extends JpaRepository<BlogPost, UUID> {
    List<BlogPost> findByUserId (UUID userId);
    Page<BlogPost> findByUserId (UUID userId, Pageable pageable);
    @Query("SELECT b FROM BlogPost b WHERE b.zoneTopic.id = :zoneTopicId ORDER BY b.creationBlogDate DESC")
    Page<BlogPost> findByZoneTopicId(UUID zoneTopicId, Pageable pageable);

    BlogPost findByUserIdAndId (UUID userId, UUID blogPostId);


}
