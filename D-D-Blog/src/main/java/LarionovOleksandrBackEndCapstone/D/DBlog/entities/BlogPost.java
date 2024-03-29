package LarionovOleksandrBackEndCapstone.D.DBlog.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "blog_post")
public class BlogPost {
    @Id
    @GeneratedValue
    private UUID id;
    private String category;
    private String title;
    private String cover;
    @Column(columnDefinition = "text")
    private String content;
    private LocalDateTime creationBlogDate;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "blogPost", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Comment> commentsList;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "blogPost",cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Like> likes;

    @JsonBackReference
    @ManyToOne
    private ZoneTopic zoneTopic;
}
