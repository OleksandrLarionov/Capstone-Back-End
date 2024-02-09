package LarionovOleksandrBackEndCapstone.D.DBlog.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
public class Like {
    @Id
    @GeneratedValue
    private UUID likeId;

    @ManyToOne
    @JsonIgnore
    private User user;

    @ManyToOne
    @JsonBackReference
    private BlogPost blogPost;
}
