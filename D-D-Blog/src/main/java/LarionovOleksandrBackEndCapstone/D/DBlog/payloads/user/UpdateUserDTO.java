package LarionovOleksandrBackEndCapstone.D.DBlog.payloads.user;

import java.time.LocalDate;

public record UpdateUserDTO(
        String name,

        String surname,

        String username,

        String email,

        String password,
        LocalDate userBirthday
) {
}
