package LarionovOleksandrBackEndCapstone.D.DBlog.payloads.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;



public record UserLogInDTO(
        @Email
        @NotEmpty
        String email,
        @NotEmpty
        String password) {
}
