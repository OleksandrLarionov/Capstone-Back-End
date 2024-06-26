package LarionovOleksandrBackEndCapstone.D.DBlog.controllers;

import LarionovOleksandrBackEndCapstone.D.DBlog.ENUMS.ROLE;
import LarionovOleksandrBackEndCapstone.D.DBlog.entities.User;
import LarionovOleksandrBackEndCapstone.D.DBlog.exceptions.BadRequestException;
import LarionovOleksandrBackEndCapstone.D.DBlog.payloads.user.CheckResponse;
import LarionovOleksandrBackEndCapstone.D.DBlog.payloads.user.UpdateUserDTO;
import LarionovOleksandrBackEndCapstone.D.DBlog.payloads.user.UserEmailDTO;
import LarionovOleksandrBackEndCapstone.D.DBlog.services.AuthService;
import LarionovOleksandrBackEndCapstone.D.DBlog.services.UserService;
import jakarta.validation.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthService authService;

    @GetMapping("/me")
    public User getProfile(@AuthenticationPrincipal User currentUser) {
        return currentUser;
    }

    @GetMapping("/me/getUsers")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<User> getAllUsers(@AuthenticationPrincipal User currentUser,
                                      @RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int size,
                                      @RequestParam(defaultValue = "email") String orderBy)
    {
        return userService.getUsers(page, size, orderBy);
    }

    @PutMapping("/me/updateProfile")
    @ResponseStatus(HttpStatus.OK)
    public User updateUser(@AuthenticationPrincipal User currentUser,
                           @RequestBody @Validated UpdateUserDTO body, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());
        } else {
            return authService.updateUser(currentUser, body);
        }
    }
    @PostMapping("/me/check")
    @ResponseStatus(HttpStatus.OK)
    public CheckResponse responseRole(@AuthenticationPrincipal User currentUser,
                                      @RequestBody @Validated UserEmailDTO payload, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());
        } else {
            User user = userService.findByEmail(payload.email());
            ROLE userRole = user.getRole();
            boolean isAdmin = userRole == ROLE.ADMIN;
            return new CheckResponse(isAdmin);
        }
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public User findById(@PathVariable UUID id) {
        return userService.findById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ADMIN')")
    public void findByIdAndDelete(@PathVariable UUID id) {
        userService.delete(id);
    }

    @DeleteMapping("/me/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('USER')")
    public void findByIdAndDeleteMe(@AuthenticationPrincipal User currentUser) {
        userService.delete(currentUser.getId());
    }

    @PostMapping("/me/uploadImage")
    public String uploadUserProfileImg(@RequestParam("profileImg") MultipartFile file,
                                       @AuthenticationPrincipal User currentUser) throws IOException {
        return userService.uploadPicture(file,currentUser.getId());
    }
    @PostMapping("/me/uploadImage/commentArea")
    public String blogBackgroundImage(@RequestParam("commentBackgroundImg") MultipartFile file,
                                      @AuthenticationPrincipal User currentUser) throws IOException {
        return userService.blogBackgroundImage(file,currentUser.getId());
    }

    @GetMapping("/lockUser/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String lockUser (@AuthenticationPrincipal User currentUser,
                            @PathVariable UUID userId){
        User user = userService.findById(userId);
        userService.validation(user.getEmail());
        if(!user.getLocked()){
            return "Account con id: " + user.getId() + " email: " + user.getEmail() + " è stato Bloccato";
        } else return "Account con id: " + user.getId() + " email: " + user.getEmail() + " è stato Sbloccato";
    }
}
