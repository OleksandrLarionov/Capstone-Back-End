package LarionovOleksandrBackEndCapstone.D.DBlog.payloads.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;



@Data
public class NewUserDTO{
        @Pattern(regexp = "^[^<>{}$`]*$", message = "invalid characters")
        private String username;
        @Pattern(regexp = "^[^<>{}$`]*$", message = "invalid characters")
        @Size(min = 2, max = 20, message = "Name must be Longer")
        private String name;
        @Pattern(regexp = "^[^<>{}$`]*$", message = "invalid characters")
        @Size(min = 2, max = 20, message = "Surname must be longer")
        private String surname;
        @NotEmpty
        @Email
        private String email;
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$")
        private String password;
        @Pattern(regexp = "^[^<>{}$`]*$", message = "invalid characters")
        @Size(min = 8, max = 20, message = "Answer must be longer")
        private String secretAnswer;
        private String profileImg;
}
