package LarionovOleksandrBackEndCapstone.D.DBlog.entities;



import LarionovOleksandrBackEndCapstone.D.DBlog.ENUMS.ROLE;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@Entity
@JsonIgnoreProperties({"password", "authorities", "accountNonExpired", "enabled", "accountNonLocked", "credentialsNonExpired", "username", "role", "secretAnswer"})
public class User implements UserDetails {
    @Id
    @GeneratedValue
    private UUID id;
    private String username;
    private String name;
    private String surname;
    private String email;
    private String password;
    private String secretAnswer;
    private String profileImage;
    private String blogBackgroundImage;
    @Enumerated(EnumType.STRING)
    private ROLE role;


    public User(
            String username, String name, String surname,
            String email, String password, String secretAnswer) {
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.secretAnswer = secretAnswer;
        this.profileImage = "https://ui-avatars.com/api/?name=" +
                name.replaceAll(" ", "") + "+" +
                surname.replaceAll(" ", "");
        this.blogBackgroundImage = "MUST TO BE SETTED";
        this.role = ROLE.USER;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.role.name()));
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}