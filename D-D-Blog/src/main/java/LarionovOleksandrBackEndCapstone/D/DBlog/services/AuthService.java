package LarionovOleksandrBackEndCapstone.D.DBlog.services;

import LarionovOleksandrBackEndCapstone.D.DBlog.ENUMS.ROLE;
import LarionovOleksandrBackEndCapstone.D.DBlog.beanConfig.MailGunSender;
import LarionovOleksandrBackEndCapstone.D.DBlog.entities.BlogPost;
import LarionovOleksandrBackEndCapstone.D.DBlog.entities.Comment;
import LarionovOleksandrBackEndCapstone.D.DBlog.entities.User;
import LarionovOleksandrBackEndCapstone.D.DBlog.exceptions.BadRequestException;
import LarionovOleksandrBackEndCapstone.D.DBlog.exceptions.UnauthorizedException;
import LarionovOleksandrBackEndCapstone.D.DBlog.payloads.user.NewUserDTO;
import LarionovOleksandrBackEndCapstone.D.DBlog.payloads.user.UpdateUserDTO;
import LarionovOleksandrBackEndCapstone.D.DBlog.payloads.user.UserLogInDTO;
import LarionovOleksandrBackEndCapstone.D.DBlog.repositories.UserRepository;
import LarionovOleksandrBackEndCapstone.D.DBlog.security.JWTTools;
import com.github.javafaker.Faker;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@Service
public class AuthService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;
    @Autowired
    private PasswordEncoder bcrypt;
    @Autowired
    private JWTTools jwtTools;
    @Autowired
    private MailGunSender mailGunSender;

    public String authenticateUser(UserLogInDTO body) {
        User user = userService.findByEmail(body.email());
        if (bcrypt.matches(body.password(), user.getPassword())) {
            return jwtTools.createToken(user);
        } else {
            throw new UnauthorizedException("Credenziali non valide!");
        }
    }

    @Transactional
    public String confirmToken(String token) {
        jwtTools.verifyToken(token);
        String userEmail = jwtTools.extractEmailFromToken(token);
        User user = userService.findByEmail(userEmail);
        userService.validation(user.getEmail());
        return "confirmed";
    }

    public User saveNewUser(NewUserDTO body) {
        Faker faker = new Faker();
        userRepository.findByEmail(body.getEmail()).ifPresent(user ->
        {
            throw new BadRequestException("l' email " + user.getEmail() + " è già in uso");
        });
        User newUser = new User();
        Random rndm = new Random();
        int random = rndm.nextInt(1, 1000000);
        String newUsername = faker.witcher().character().split(" ")[0] + random;
        String finalUsername = newUsername;
        int suffix = 1;
        while (userRepository.findByUsername(finalUsername) != null) {
            finalUsername = newUsername + suffix;
            suffix++;
        }
        newUser.setUsername(finalUsername);
        newUser.setName(body.getName());
        newUser.setSurname(body.getSurname());
        newUser.setEmail(body.getEmail());
        newUser.setLocked(true);
        List<BlogPost> blogPostList = new ArrayList<>();
        List<Comment> commentsList = new ArrayList<>();
        newUser.setBlogPostList(blogPostList);
        newUser.setCommentsList(commentsList);
        newUser.setUserCreationDate(LocalDate.now());
        newUser.setUserBirthday(body.getUserBirthday());
        if (body.getPassword() != null) {
            newUser.setPassword(
                    bcrypt.encode(body.getPassword())
            );
        }
        newUser.setRole(ROLE.USER);
        if (body.getProfileImg() != null) {
            newUser.setProfileImage(body.getProfileImg());
        } else {
            newUser.setProfileImage("https://ui-avatars.com/api/?name=" +
                    body.getName().replaceAll(" ", "") + "+" +
                    body.getSurname().replaceAll(" ", ""));
        }
        newUser.setBlogBackgroundImage("MUST TO BE SETTED"); //TODO : must to add a possibility to change a background image
        if (body.getSecretAnswer() != null && !body.getSecretAnswer().isEmpty()) {
            newUser.setSecretAnswer(bcrypt.encode(body.getSecretAnswer()));
        }
        String token = jwtTools.createValidationToken(newUser);
        mailGunSender.sendMail(body.getEmail(), body, token);
        return userRepository.save(newUser);
    }
    public User updateUser(User currentUser, UpdateUserDTO body) {
        User found = userService.findById(currentUser.getId());
        if (body.name() != null) {
            if (!body.name().isEmpty()) {
                found.setName(body.name());
            }
        }
        if (body.surname() != null) {
            if (!body.surname().isEmpty()) {
                found.setSurname(body.surname());
            }
        }
        if (body.username() != null) {
            if (!body.username().isEmpty()) {
                found.setUsername(body.username());
            }
        }
        if (body.email() != null) {
            if (!body.email().isEmpty()) {
                found.setEmail(body.email());
            }
        }
        if (body.password() != null) {
            if (!body.password().isEmpty()) {
                found.setPassword(bcrypt.encode(body.password()));
            }
            if (body.userBirthday() != null) {
                found.setUserBirthday(body.userBirthday());
            }
        }
        return userRepository.save(found);
    }

}
