package LarionovOleksandrBackEndCapstone.D.DBlog.repositories;

import LarionovOleksandrBackEndCapstone.D.DBlog.entities.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CommentRepository extends JpaRepository<Comment, UUID> {
    Page<Comment> findByBlogPostId(UUID blogPostId, Pageable pageable);
}
