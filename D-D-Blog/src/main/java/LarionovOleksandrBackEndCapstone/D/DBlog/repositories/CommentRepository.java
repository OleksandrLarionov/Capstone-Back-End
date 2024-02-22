package LarionovOleksandrBackEndCapstone.D.DBlog.repositories;

import LarionovOleksandrBackEndCapstone.D.DBlog.entities.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CommentRepository extends JpaRepository<Comment, UUID> {
    @Query("SELECT c FROM Comment c WHERE c.blogPost.id = :blogPostId ORDER BY c.date DESC")
    Page<Comment> findByBlogPostId(UUID blogPostId, Pageable pageable);
    List<Comment> findByUserId (UUID userId);
    List<Comment> findByBlogPostId (UUID blogPostId);
}
