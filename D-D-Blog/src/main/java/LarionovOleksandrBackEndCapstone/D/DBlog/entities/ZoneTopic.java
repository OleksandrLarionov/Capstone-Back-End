package LarionovOleksandrBackEndCapstone.D.DBlog.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "zone_topic")
@Getter
@Setter
public class ZoneTopic {
    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    @Column(columnDefinition = "text")
    private String description;

    @JsonBackReference
    @ManyToOne
    private Zone zone;

    @JsonManagedReference
    @OneToMany(mappedBy = "zoneTopic", cascade = CascadeType.ALL)
    private List<BlogPost> blogPostList;
}
