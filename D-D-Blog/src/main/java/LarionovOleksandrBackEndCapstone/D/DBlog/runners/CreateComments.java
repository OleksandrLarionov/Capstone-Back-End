package LarionovOleksandrBackEndCapstone.D.DBlog.runners;

import LarionovOleksandrBackEndCapstone.D.DBlog.entities.BlogPost;
import LarionovOleksandrBackEndCapstone.D.DBlog.entities.User;
import LarionovOleksandrBackEndCapstone.D.DBlog.payloads.CommentDTO;
import LarionovOleksandrBackEndCapstone.D.DBlog.repositories.BlogPostRepository;
import LarionovOleksandrBackEndCapstone.D.DBlog.repositories.UserRepository;
import LarionovOleksandrBackEndCapstone.D.DBlog.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.UUID;

@Component
@Order(3)
public class CreateComments implements CommandLineRunner {
    @Autowired
    UserRepository userRepository;
    @Autowired
    BlogPostRepository blogPostRepository;
    @Autowired
    CommentService commentService;
    @Override
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        boolean errors = false;
        do {
            System.out.println("Vuoi Procedere Con la Creazione Degli Blogs (y/n)");
            String choice = scanner.nextLine();
            switch (choice.toLowerCase()) {
                case "y" -> {
                    createComments();
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
        for (int i = 0; i < 20; i++){
            UUID idBlog =getRandomBlogPost().getId();
                    CommentDTO newComment = new CommentDTO(

                    "Mitico",
                    getRandomUserDB().getId(),
                            idBlog
            );
            commentService.saveComment(newComment, idBlog);
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
