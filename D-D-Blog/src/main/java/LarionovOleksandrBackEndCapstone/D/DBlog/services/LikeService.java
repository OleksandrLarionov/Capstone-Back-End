package LarionovOleksandrBackEndCapstone.D.DBlog.services;

import LarionovOleksandrBackEndCapstone.D.DBlog.entities.BlogPost;
import LarionovOleksandrBackEndCapstone.D.DBlog.entities.Like;
import LarionovOleksandrBackEndCapstone.D.DBlog.entities.User;
import LarionovOleksandrBackEndCapstone.D.DBlog.exceptions.NotFoundException;
import LarionovOleksandrBackEndCapstone.D.DBlog.repositories.LikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class LikeService {
    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private BlogPostService blogPostService;

    public Like addLike(Like like, UUID userId, UUID blogPostId) throws NotFoundException {
        User user = userService.findById(userId);
        if (user == null) {
            throw new NotFoundException("User not found with id: " + userId);
        }
        BlogPost blogPost = blogPostService.findById(blogPostId);
        if (blogPost == null) {
            throw new NotFoundException("Blog post not found with id: " + blogPostId);
        }

        Like likeCheck = likeRepository.findByUserIdAndBlogPostId(userId, blogPostId);
        if (likeCheck != null) {
            throw new RuntimeException("You have already liked this article");
        }

        like.setBlogPost(blogPost);
        like.setUser(user);
        return likeRepository.save(like);
    }
    public void deleteLike(UUID blogPostId, UUID userId) {
        Like like = likeRepository.findByUserIdAndBlogPostId(blogPostId, userId);
        if (like != null) {
            like.getBlogPost().getLikes().remove(like);
            likeRepository.delete(like);
        }

    }
}
