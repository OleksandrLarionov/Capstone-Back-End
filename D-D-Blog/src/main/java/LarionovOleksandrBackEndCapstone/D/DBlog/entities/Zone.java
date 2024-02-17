package LarionovOleksandrBackEndCapstone.D.DBlog.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Getter
@Setter
@Table(name = "zone")
public class Zone {
    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    private int orderNumber;
    @JsonManagedReference
    @OneToMany(mappedBy = "zone", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ZoneTopic> zoneTopicList;
}
