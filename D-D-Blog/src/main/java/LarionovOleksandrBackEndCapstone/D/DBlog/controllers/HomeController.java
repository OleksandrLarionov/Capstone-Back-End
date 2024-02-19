package LarionovOleksandrBackEndCapstone.D.DBlog.controllers;



import LarionovOleksandrBackEndCapstone.D.DBlog.entities.BlogPost;
import LarionovOleksandrBackEndCapstone.D.DBlog.entities.User;
import LarionovOleksandrBackEndCapstone.D.DBlog.entities.Zone;
import LarionovOleksandrBackEndCapstone.D.DBlog.services.BlogPostService;
import LarionovOleksandrBackEndCapstone.D.DBlog.services.ZoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/home")
public class HomeController {
    @Autowired
    private ZoneService zoneService;
    @Autowired
    private BlogPostService blogPostService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<Zone> getHome(
            @AuthenticationPrincipal User currentUser,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "orderNumber") String sortBy) {
        return zoneService.getAllZone(page, size, sortBy);
    }


    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/topic/{id}")
    public Page<BlogPost> getAllBlogsByTopicZoneId(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "creationBlogDate") String sortBy,
            @PathVariable UUID id,
            @AuthenticationPrincipal User currentUser
    ) {
        return blogPostService.getAllBlogsByZoneTopicId(id, page, size, sortBy);
    }

}
