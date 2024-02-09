package LarionovOleksandrBackEndCapstone.D.DBlog.services;

import LarionovOleksandrBackEndCapstone.D.DBlog.entities.BlogPost;
import LarionovOleksandrBackEndCapstone.D.DBlog.entities.Comment;
import LarionovOleksandrBackEndCapstone.D.DBlog.exceptions.NotFoundException;
import LarionovOleksandrBackEndCapstone.D.DBlog.payloads.CommentDTO;
import LarionovOleksandrBackEndCapstone.D.DBlog.repositories.BlogPostRepository;
import LarionovOleksandrBackEndCapstone.D.DBlog.repositories.CommentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private BlogPostService blogPostService;

    public Page<Comment> getAllComments (int page, int size, String orderBy) {
        if (size < 0)
            size = 10;
        if (size > 100)
            size = 20;
        Pageable pageable = PageRequest.of(page, size, Sort.by(orderBy));
        return commentRepository.findAll(pageable);
    }
    public Comment saveComment(CommentDTO body, UUID blogPostId) {
        Comment newComment = new Comment();
        newComment.setComment(body.comment());
        newComment.setDate(LocalDate.now());
        BlogPost found = blogPostService.findById(blogPostId);
        newComment.setBlogPost(found);
        return commentRepository.save(newComment);
    }

    public Comment findById(UUID id) {
        return commentRepository.findById(id).orElseThrow(() -> new NotFoundException(String.valueOf(id)));
    }
    @Transactional
    public void delete(UUID id) {
        Comment found = this.findById(id);
        commentRepository.delete(found);
    }
    public Page<Comment> findCommentsByBlogPostId (UUID blogPostId,int page, int size, String orderBy) {
        if (size < 0)
            size = 10;
        if (size > 100)
            size = 20;
        Pageable pageable = PageRequest.of(page, size, Sort.by(orderBy));
        return commentRepository.findByBlogPostId(blogPostId, pageable);
    }
    public Comment upgradeComment (CommentDTO comment, UUID commentId){
        Comment found = this.findById(commentId);
        found.setComment(comment.comment());
        return commentRepository.save(found);
    }

}
