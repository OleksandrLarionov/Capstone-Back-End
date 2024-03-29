package LarionovOleksandrBackEndCapstone.D.DBlog.controllers;


import LarionovOleksandrBackEndCapstone.D.DBlog.ENUMS.ROLE;
import LarionovOleksandrBackEndCapstone.D.DBlog.entities.BlogPost;
import LarionovOleksandrBackEndCapstone.D.DBlog.entities.Comment;
import LarionovOleksandrBackEndCapstone.D.DBlog.entities.User;
import LarionovOleksandrBackEndCapstone.D.DBlog.exceptions.BadRequestException;
import LarionovOleksandrBackEndCapstone.D.DBlog.exceptions.UnauthorizedException;
import LarionovOleksandrBackEndCapstone.D.DBlog.payloads.AutPayload;
import LarionovOleksandrBackEndCapstone.D.DBlog.payloads.user.NewUserDTO;
import LarionovOleksandrBackEndCapstone.D.DBlog.payloads.user.NewUserResponseDTO;
import LarionovOleksandrBackEndCapstone.D.DBlog.payloads.user.UserLogInDTO;
import LarionovOleksandrBackEndCapstone.D.DBlog.payloads.user.UserLogInResponseDTO;
import LarionovOleksandrBackEndCapstone.D.DBlog.repositories.UserRepository;
import LarionovOleksandrBackEndCapstone.D.DBlog.security.JWTTools;
import LarionovOleksandrBackEndCapstone.D.DBlog.services.AuthService;
import LarionovOleksandrBackEndCapstone.D.DBlog.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder bcrypt;
    @Autowired
    private JWTTools jwtTools;

    @PostMapping("/login")
    public UserLogInResponseDTO UserLogInResponseDTO(@RequestBody @Validated UserLogInDTO body, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());
        } else {
            String accessToken = authService.authenticateUser(body);
            return new UserLogInResponseDTO(accessToken);
        }
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public NewUserResponseDTO saveUser(@RequestBody @Validated NewUserDTO body, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());
        } else {
            userRepository.findByEmail(body.getEmail()).ifPresent(user ->
            {
                throw new BadRequestException("l' email " + user.getEmail() + " è già in uso");
            });
            User newUser = authService.saveNewUser(body);
            return new NewUserResponseDTO(newUser.getId());
        }
    }
/*@PostMapping("/verify-token")
public ResponseEntity<String> checkToken(@RequestBody AutPayload token) {
    try {
        jwtTools.verifyToken(token.accessToken());
        return ResponseEntity.ok("Token valido");
    } catch (UnauthorizedException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante la verifica del token");
    }
}*/
}
