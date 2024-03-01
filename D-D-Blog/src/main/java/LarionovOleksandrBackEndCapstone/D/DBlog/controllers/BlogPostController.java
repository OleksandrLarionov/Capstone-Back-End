package LarionovOleksandrBackEndCapstone.D.DBlog.controllers;

import LarionovOleksandrBackEndCapstone.D.DBlog.entities.BlogPost;
import LarionovOleksandrBackEndCapstone.D.DBlog.entities.Comment;
import LarionovOleksandrBackEndCapstone.D.DBlog.entities.Like;
import LarionovOleksandrBackEndCapstone.D.DBlog.entities.User;
import LarionovOleksandrBackEndCapstone.D.DBlog.exceptions.BadRequestException;
import LarionovOleksandrBackEndCapstone.D.DBlog.exceptions.NotFoundException;
import LarionovOleksandrBackEndCapstone.D.DBlog.payloads.BlogPostDTO;
import LarionovOleksandrBackEndCapstone.D.DBlog.payloads.CommentDTO;
import LarionovOleksandrBackEndCapstone.D.DBlog.repositories.LikeRepository;
import LarionovOleksandrBackEndCapstone.D.DBlog.services.BlogPostService;
import LarionovOleksandrBackEndCapstone.D.DBlog.services.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/blogposts")
public class BlogPostController {
    @Autowired
    private BlogPostService blogPostService;
    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private LikeService likeService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<BlogPost> getAllBlogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "creationBlogDate") String sortBy,
            @AuthenticationPrincipal User currentUser
            ) {
        return blogPostService.getAllBlogs(page, size, sortBy);
    }

    @GetMapping("/me/myblogs")
    public Page<BlogPost> getUserBlogs(
            @AuthenticationPrincipal User currentUser,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "creationBlogDate") String orderBy) {
        return blogPostService.findCurrentUserBlogs(currentUser, page, size, orderBy);
    }
    @GetMapping("me/likes/{id}")
    public int likeCounter(@AuthenticationPrincipal User currentUser, @PathVariable UUID id){
        return likeRepository.countByBlogPostId(id);
    }
    @PostMapping("/me/topic/{id}")
    public void addLike (@AuthenticationPrincipal User currentUser, @PathVariable UUID id){
        Like newLike = new Like();
        likeService.addLike(newLike,currentUser.getId(),id);
    }
    @DeleteMapping("/me/like/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeLike(@PathVariable UUID id, @AuthenticationPrincipal User currentUser) {
        likeService.deleteLike(currentUser.getId(), id);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BlogPost getBlogById (@AuthenticationPrincipal User currentUser, @PathVariable UUID id){
       return blogPostService.findById(id);
    }

    @PostMapping("/topic/addNewPost")
    @ResponseStatus(HttpStatus.CREATED)
    public BlogPost createBlogPost(
            @AuthenticationPrincipal User currentUSer,
            @RequestBody @Validated BlogPostDTO payload,
            BindingResult validation) throws NotFoundException {
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());
        } else {
            return blogPostService.saveBlogPost(payload);
        }
    }

    @DeleteMapping("/me/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findByIdAndDeleteCurrentUserBlog(@PathVariable UUID id, @AuthenticationPrincipal User currentUser) {
        blogPostService.deleteBlog(currentUser, id);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ADMIN')")
    public void findByIdAndDelete(@PathVariable UUID id) {
        blogPostService.delete(id);
    }

}
