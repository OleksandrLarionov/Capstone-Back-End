package LarionovOleksandrBackEndCapstone.D.DBlog.services;


import LarionovOleksandrBackEndCapstone.D.DBlog.entities.BlogPost;
import LarionovOleksandrBackEndCapstone.D.DBlog.entities.Comment;
import LarionovOleksandrBackEndCapstone.D.DBlog.entities.User;
import LarionovOleksandrBackEndCapstone.D.DBlog.entities.ZoneTopic;
import LarionovOleksandrBackEndCapstone.D.DBlog.exceptions.NotFoundException;
import LarionovOleksandrBackEndCapstone.D.DBlog.exceptions.UnauthorizedException;
import LarionovOleksandrBackEndCapstone.D.DBlog.payloads.BlogPostDTO;
import LarionovOleksandrBackEndCapstone.D.DBlog.repositories.BlogPostRepository;
import LarionovOleksandrBackEndCapstone.D.DBlog.repositories.CommentRepository;
import LarionovOleksandrBackEndCapstone.D.DBlog.repositories.UserRepository;
import LarionovOleksandrBackEndCapstone.D.DBlog.repositories.ZoneTopicRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BlogPostService {
    @Autowired
    private BlogPostRepository blogPostRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ZoneTopicRepository zoneTopicRepository;

    public Page<BlogPost> getAllBlogs(int page, int size, String orderBy) {
        if (size < 0)
            size = 10;
        if (size > 100)
            size = 20;
        Pageable pageable = PageRequest.of(page, size, Sort.by(orderBy));
        return blogPostRepository.findAll(pageable);
    }

    public Page<BlogPost> findCurrentUserBlogs(User currentUser, int page, int size, String orderBy) {
        if (size < 0)
            size = 10;
        if (size > 100)
            size = 20;
        Pageable pageable = PageRequest.of(page, size, Sort.by(orderBy));
        return blogPostRepository.findByUserId(currentUser.getId(), pageable);
    }

    public Page<BlogPost> getAllBlogsByZoneTopicId(UUID zoneTopicId, int page, int size, String orderBy) {
        if (size < 0)
            size = 10;
        if (size > 100)
            size = 20;
        Pageable pageable = PageRequest.of(page, size, Sort.by(orderBy));
        return blogPostRepository.findByZoneTopicId(zoneTopicId, pageable);
    }

    public List<BlogPost> getAll() {
        return blogPostRepository.findAll();
    }

    public BlogPost saveBlogPost(BlogPostDTO body) {
        BlogPost newBlog = new BlogPost();
        newBlog.setTitle(body.title());
        newBlog.setCategory(body.category());
        if(body.cover() != null){
            newBlog.setCover(body.cover());
        } else {
         newBlog.setCover("http://res.cloudinary.com/dfzqwrrvb/image/upload/v1709294064/vyu1s5ujdfzrgxvzdeh2.webp");
        }
        newBlog.setContent(body.content());
        newBlog.setCreationBlogDate(LocalDateTime.now());
        User user = userRepository.findById(body.userId()).orElseThrow(() -> new NotFoundException("Utente non trovato"));
        List<BlogPost> blogsUser = blogPostRepository.findByUserId(body.userId());
        blogsUser.add(newBlog);
        user.setBlogPostList(blogsUser);
        newBlog.setUser(user);
        ZoneTopic topic = zoneTopicRepository.findById(body.topicZoneId()).orElseThrow(() -> new NotFoundException("Topic non trovato"));
        newBlog.setZoneTopic(topic);
        return blogPostRepository.save(newBlog);
    }


    public BlogPost findById(UUID id) {
        return blogPostRepository.findById(id).orElseThrow(() -> new NotFoundException(String.valueOf(id)));
    }

    @Transactional
    public void delete(UUID id) {
        BlogPost found = this.findById(id);
        blogPostRepository.delete(found);
    }

    public BlogPost findByIdAndUpdate(UUID id, BlogPostDTO body) {
        BlogPost found = this.findById(id);
        found.setId(id);
        found.setCategory(body.category());
        found.setTitle(body.title());
        found.setCover(body.cover());
        found.setContent(body.content());
        return blogPostRepository.save(found);

    }

    public List<BlogPost> filterByCategory(String categoria) {
        List<BlogPost> categoryList = this.getAll().stream()
                .filter(blogPost -> blogPost.getCategory().equals(categoria))
                .collect(Collectors.toList());
        return categoryList;
    }
@Transactional
    public void deleteBlog(User currentUser, UUID id) {
        BlogPost found = this.findById(id);
        if (!found.getUser().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException("Non sei autorizzato a cancellare questo post");
        }
         blogPostRepository.delete(found);
    }

}

