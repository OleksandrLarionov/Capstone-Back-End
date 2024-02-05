package LarionovOleksandrBackEndCapstone.D.DBlog.payloads.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record NewUserDTO(
        @NotEmpty
        String username,
        @NotEmpty(message = "Can not be empty")
        @Size(min = 2, max = 20, message = "Name must be Longer")
        String name,
        @NotEmpty(message = "Can not be empty")
        @Size(min = 2, max = 20, message = "Surname must be longer")
        String surname,
        @NotEmpty
        @Email
        String email,
        @NotEmpty
        String password,
        @NotEmpty
        @Size(min = 8, max = 20, message = "Answer must be longer")
        String secretAnswer
        ) {
}
