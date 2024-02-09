package LarionovOleksandrBackEndCapstone.D.DBlog.controllers;

import LarionovOleksandrBackEndCapstone.D.DBlog.entities.Like;
import LarionovOleksandrBackEndCapstone.D.DBlog.exceptions.NotFoundException;
import LarionovOleksandrBackEndCapstone.D.DBlog.services.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/likes")
public class LikeController {
    @Autowired
    private LikeService likeService;

    @PostMapping("/{blogPostId}/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Like addLike(@RequestBody Like like, @PathVariable("blogPostId") UUID blogPostId, @PathVariable("userId") UUID userId) throws NotFoundException {
        return likeService.addLike(like, blogPostId, userId);
    }

    @DeleteMapping("/{blogPostId}/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLike(@PathVariable("blogPostId") UUID blogPostId, @PathVariable("userId") UUID userId) throws NotFoundException {
        likeService.deleteLike(blogPostId, userId);
    }
}
