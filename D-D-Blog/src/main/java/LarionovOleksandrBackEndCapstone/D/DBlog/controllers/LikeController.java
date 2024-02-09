package LarionovOleksandrBackEndCapstone.D.DBlog.controllers;

import LarionovOleksandrBackEndCapstone.D.DBlog.entities.Like;
import LarionovOleksandrBackEndCapstone.D.DBlog.exceptions.NotFoundException;
import LarionovOleksandrBackEndCapstone.D.DBlog.services.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/likes")
public class LikeController {
    @Autowired
    private LikeService likeService;

    @PostMapping("/{blogPostId}/{userId}")
    public Like addLike(@RequestBody Like like, @PathVariable("blogPostId") UUID blogPostId, @PathVariable("userId") UUID userId) throws NotFoundException {
        return likeService.addLike(like, blogPostId, userId);
    }

    @DeleteMapping("/{blogPostId}/{userId}")
    public void deleteLike(@PathVariable("blogPostId") UUID blogPostId, @PathVariable("userId") UUID userId) throws NotFoundException {
        likeService.deleteLike(blogPostId, userId);
    }
}
