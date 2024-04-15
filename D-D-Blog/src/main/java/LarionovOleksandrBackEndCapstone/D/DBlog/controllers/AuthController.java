package LarionovOleksandrBackEndCapstone.D.DBlog.controllers;




import LarionovOleksandrBackEndCapstone.D.DBlog.entities.User;
import LarionovOleksandrBackEndCapstone.D.DBlog.exceptions.BadRequestException;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;



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

    @GetMapping("/register/confirm/{token}")
    public String confirm(@PathVariable String token){
        System.out.println("sono nella get");
        return authService.confirmToken(token);
    };

}
