package LarionovOleksandrBackEndCapstone.D.DBlog.services;

import LarionovOleksandrBackEndCapstone.D.DBlog.entities.BlogPost;
import LarionovOleksandrBackEndCapstone.D.DBlog.entities.Comment;
import LarionovOleksandrBackEndCapstone.D.DBlog.entities.User;
import LarionovOleksandrBackEndCapstone.D.DBlog.exceptions.NotFoundException;
import LarionovOleksandrBackEndCapstone.D.DBlog.payloads.CommentDTO;
import LarionovOleksandrBackEndCapstone.D.DBlog.repositories.BlogPostRepository;
import LarionovOleksandrBackEndCapstone.D.DBlog.repositories.CommentRepository;
import LarionovOleksandrBackEndCapstone.D.DBlog.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private BlogPostService blogPostService;
    @Autowired
    private UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    BlogPostRepository blogPostRepository;

    public Page<Comment> getAllComments(int page, int size, String orderBy) {
        if (size < 0)
            size = 10;
        if (size > 100)
            size = 20;
        Pageable pageable = PageRequest.of(page, size, Sort.by(orderBy));
        return commentRepository.findAll(pageable);
    }

    public Comment saveComment(CommentDTO body, UUID blogId, UUID userId) {
        User user = userService.findById(userId);
        if (user == null) {
            throw new NotFoundException("User not found with id: " + userId);
        }
        BlogPost blogPost = blogPostService.findById(body.blogId());
        if (blogPost == null) {
            throw new NotFoundException("Blog post not found with id: " + blogId);
        }
        List<Comment> blogPostCommentsList = commentRepository.findByBlogPostId(blogId);
        List<Comment> commentsUserList = commentRepository.findByUserId(userId);
        Comment newComment = new Comment();
        newComment.setDate(LocalDateTime.now());
        newComment.setComment(body.comment());
        commentsUserList.add(newComment);
        blogPostCommentsList.add(newComment);
        user.setCommentsList(commentsUserList);
        blogPost.setCommentsList(blogPostCommentsList);
        newComment.setBlogPost(blogPost);
        newComment.setUser(user);
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

    public Page<Comment> findCommentsByBlogPostId(UUID blogPostId, int page, int size, String orderBy) {
        if (size < 0)
            size = 10;
        if (size > 100)
            size = 20;
        Pageable pageable = PageRequest.of(page, size, Sort.by(orderBy));
        return commentRepository.findByBlogPostId(blogPostId, pageable);
    }


    public Comment upgradeComment(CommentDTO comment, UUID commentId) {
        Comment found = this.findById(commentId);
        found.setComment(comment.comment());
        return commentRepository.save(found);
    }

}
