package LarionovOleksandrBackEndCapstone.D.DBlog.controllers;

import LarionovOleksandrBackEndCapstone.D.DBlog.entities.Comment;
import LarionovOleksandrBackEndCapstone.D.DBlog.exceptions.BadRequestException;
import LarionovOleksandrBackEndCapstone.D.DBlog.exceptions.NotFoundException;
import LarionovOleksandrBackEndCapstone.D.DBlog.payloads.CommentDTO;
import LarionovOleksandrBackEndCapstone.D.DBlog.payloads.CommentResponseDTO;
import LarionovOleksandrBackEndCapstone.D.DBlog.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/comments")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @GetMapping("/blogPost/{blogPostId}")
    @ResponseStatus(HttpStatus.OK)
    public Page<Comment> getCommentsByBlogPostId(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "username") String sortBy,
            @PathVariable UUID blogPostId) {
        return commentService.findCommentsByBlogPostId(blogPostId, page, size, sortBy);
    }


    @PostMapping("/blogPost/{blogPostId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Comment createComment(
            @PathVariable UUID blogPostId,
            @RequestBody @Validated CommentDTO payload,
            BindingResult validation) throws NotFoundException {
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());
        } else {
            return commentService.saveComment(payload, blogPostId);
        }
    }


    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Comment> getCommentById(@PathVariable UUID id) throws NotFoundException {
        Comment comment = commentService.findById(id);
        return ResponseEntity.ok(comment);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CommentResponseDTO updateComment(
            @PathVariable UUID id,
            @RequestBody @Validated CommentDTO payload, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());
        } else {
            commentService.upgradeComment(payload, id);
            return new CommentResponseDTO("Comment Updated");
        }
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ADMIN')")
    public void findByIdAndDelete(@PathVariable UUID id) {
        commentService.delete(id);
    }
}
