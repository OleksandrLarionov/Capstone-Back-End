package LarionovOleksandrBackEndCapstone.D.DBlog.payloads.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record UpdateUserDTO(
        @Pattern(regexp = "^[^<>{}$`]*$", message = "invalid characters")
        @Size(min = 2, max = 20, message = "Name must be Longer")
        String name,
        @Pattern(regexp = "^[^<>{}$`]*$", message = "invalid characters")
        @Size(min = 2, max = 20, message = "Surname must be longer")
        String surname,
        @Pattern(regexp = "^[^<>{}$`]*$", message = "invalid characters")
        String username,
        @Email
        String email,

        String password,
        LocalDate userBirthday
) {
}
