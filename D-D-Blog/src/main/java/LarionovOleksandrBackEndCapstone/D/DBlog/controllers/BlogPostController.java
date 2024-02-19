package LarionovOleksandrBackEndCapstone.D.DBlog.controllers;

import LarionovOleksandrBackEndCapstone.D.DBlog.entities.BlogPost;
import LarionovOleksandrBackEndCapstone.D.DBlog.entities.User;
import LarionovOleksandrBackEndCapstone.D.DBlog.services.BlogPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/blogposts")
public class BlogPostController {
    @Autowired
    private BlogPostService blogPostService;

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
