package LarionovOleksandrBackEndCapstone.D.DBlog.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

import static LarionovOleksandrBackEndCapstone.D.DBlog.exceptions.ExceptionsHandler.newDateAndHour;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "comments_area")
public class Comment {
    @Id
    @GeneratedValue
    private UUID id;
    private String comment;
    private LocalDate date = LocalDate.now();

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "blog_post_id")
    private BlogPost blogPost;
}
