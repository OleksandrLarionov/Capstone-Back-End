package LarionovOleksandrBackEndCapstone.D.DBlog.entities;



import LarionovOleksandrBackEndCapstone.D.DBlog.ENUMS.ROLE;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@Entity
@JsonIgnoreProperties({"password", "authorities", "accountNonExpired", "enabled", "accountNonLocked",
        "credentialsNonExpired", "role", "secretAnswer","blogPostList","commentsList"})
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
    private LocalDate userCreationDate;
    private LocalDate userBirthday;
    private Boolean locked;
    private Boolean enabled;
    @Enumerated(EnumType.STRING)
    private ROLE role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<BlogPost> blogPostList;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Comment> commentsList;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private ConfirmValidationToken confirmValidationToken;

    public User(
            String username, String name, String surname,
            String email, String password, String secretAnswer, Boolean enabled, Boolean locked) {
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
        this.blogPostList = new ArrayList<>();
        this.commentsList = new ArrayList<>();
        this.userCreationDate = LocalDate.now();
        this.locked = false;
        this.enabled = false;
    }

    public void addCommentToList (Comment comment){
        this.commentsList.add(comment);
    }
    public void addBlogToList (BlogPost post){
        this.blogPostList.add(post);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public String toString() {
        return "User{" +
                "userBirthday=" + userBirthday +
                '}';
    }
}
