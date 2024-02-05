package LarionovOleksandrBackEndCapstone.D.DBlog.services;

import LarionovOleksandrBackEndCapstone.D.DBlog.entities.User;
import LarionovOleksandrBackEndCapstone.D.DBlog.exceptions.BadRequestException;
import LarionovOleksandrBackEndCapstone.D.DBlog.exceptions.UnauthorizedException;
import LarionovOleksandrBackEndCapstone.D.DBlog.payloads.user.NewUserDTO;
import LarionovOleksandrBackEndCapstone.D.DBlog.payloads.user.UpdateUserDTO;
import LarionovOleksandrBackEndCapstone.D.DBlog.payloads.user.UserLogInDTO;
import LarionovOleksandrBackEndCapstone.D.DBlog.repositories.UserRepository;
import LarionovOleksandrBackEndCapstone.D.DBlog.security.JWTTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    public String authenticateUser(UserLogInDTO body) {
        User user = userService.findByEmail(body.email());
        if (bcrypt.matches(body.password(), user.getPassword())) {
            return jwtTools.createToken(user);
        } else {
            throw new UnauthorizedException("Credenziali non valide!");
        }
    }
    public User saveNewUser(NewUserDTO body) {

        userRepository.findByEmail(body.email()).ifPresent(user ->
        {
            throw new BadRequestException("Email: " + user.getEmail() + " is already in use");
        });
        User newUser = new User();
        newUser.setUsername(body.username());
        newUser.setName(body.name());
        newUser.setSurname(body.surname());
        newUser.setEmail(body.email());
        newUser.setPassword(
                bcrypt.encode(body.password())
        );
        newUser.setProfileImage("https://ui-avatars.com/api/?name=" +
                body.name().replaceAll(" ", "") + "+" +
                body.surname().replaceAll(" ", ""));
        newUser.setBlogBackgroundImage( "MUST TO BE SETTED");
        newUser.setSecretAnswer(bcrypt.encode(body.secretAnswer()));
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
        }
        return userRepository.save(found);
    }
}
