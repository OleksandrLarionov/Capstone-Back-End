package LarionovOleksandrBackEndCapstone.D.DBlog.runners;

import LarionovOleksandrBackEndCapstone.D.DBlog.entities.BlogPost;
import LarionovOleksandrBackEndCapstone.D.DBlog.entities.Like;
import LarionovOleksandrBackEndCapstone.D.DBlog.entities.User;
import LarionovOleksandrBackEndCapstone.D.DBlog.payloads.CommentDTO;
import LarionovOleksandrBackEndCapstone.D.DBlog.repositories.BlogPostRepository;
import LarionovOleksandrBackEndCapstone.D.DBlog.repositories.UserRepository;
import LarionovOleksandrBackEndCapstone.D.DBlog.services.CommentService;
import LarionovOleksandrBackEndCapstone.D.DBlog.services.LikeService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.UUID;

@Component
@Order(4)
public class CreateComments implements CommandLineRunner {
    @Value("${app.init.createCommentsEnabled:false}")
    private boolean createCommentsEnabled;
    @Autowired
    UserRepository userRepository;
    @Autowired
    BlogPostRepository blogPostRepository;
    @Autowired
    CommentService commentService;
    @Autowired
    LikeService likeService;
    @Override
    public void run(String... args) throws Exception {
        if (!createCommentsEnabled) {
            return;
        }
        Scanner scanner = new Scanner(System.in);
        boolean errors = false;
        do {
            System.out.println("Vuoi Procedere Con la Creazione Degli Commenti e Likes (y/n)");
            String choice = scanner.nextLine();
            switch (choice.toLowerCase()) {
                case "y" -> {
                    createComments();
                    addLike();
                    errors = false;
                }
                case "n" -> errors = false;
                default -> {
                    System.out.println("Input non valido. Riprova.");
                    errors = true;
                }
            }
        } while (errors);
    }

    private void createComments() {
        for (int i = 0; i < 900; i++){
            UUID idBlog =getRandomBlogPost().getId();
                    CommentDTO newComment = new CommentDTO(
                    "Mitico!",
                            idBlog
            );
            commentService.saveComment(newComment, idBlog, getRandomUserDB().getId());
        }

    }
    @Transactional
    private void addLike () {
        List<User> userList = userRepository.findAll();
        for (User user : userList) {
            User userDB = getRandomUserDB();
            BlogPost blogPost = getRandomBlogPost();

            boolean alreadyLiked = blogPost.getLikes().stream()
                    .map(Like::getUser)
                    .anyMatch(likedUser -> likedUser.getId().equals(userDB.getId()));

            if (!alreadyLiked) {
                Like newLike = new Like();
                newLike.setUser(userDB);
                newLike.setBlogPost(blogPost);
                likeService.addLike(newLike, userDB.getId(), blogPost.getId());
            }
        }
    }
    public User getRandomUserDB() {
        List<User> userList = userRepository.findAll();
        Random rmd = new Random();
        int rmdIndex = rmd.nextInt(userList.size());
        return userList.get(rmdIndex);
    }
    public BlogPost getRandomBlogPost() {
        List<BlogPost> blogPostList = blogPostRepository.findAll();
        Random rmd = new Random();
        int rmdIndex = rmd.nextInt(blogPostList.size());
        return blogPostList.get(rmdIndex);
    }

}
