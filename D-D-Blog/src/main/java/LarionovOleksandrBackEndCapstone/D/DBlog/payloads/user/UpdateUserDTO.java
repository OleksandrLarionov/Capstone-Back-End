package LarionovOleksandrBackEndCapstone.D.DBlog.payloads.user;

public record UpdateUserDTO(
        String name,

        String surname,

        String username,

        String email,

        String password){
}
