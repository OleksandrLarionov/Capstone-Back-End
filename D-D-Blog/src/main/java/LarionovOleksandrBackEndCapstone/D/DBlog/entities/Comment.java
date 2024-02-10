package LarionovOleksandrBackEndCapstone.D.DBlog.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static LarionovOleksandrBackEndCapstone.D.DBlog.exceptions.ExceptionsHandler.newDateAndHour;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "comments_area")
@Getter
@Setter
public class Comment {
    @Id
    @GeneratedValue
    private UUID id;
    @Column(columnDefinition = "text")
    private String comment;
    private LocalDateTime date;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "blog_post_id")
    private BlogPost blogPost;
}
