package LarionovOleksandrBackEndCapstone.D.DBlog.repositories;

import LarionovOleksandrBackEndCapstone.D.DBlog.entities.ZoneTopic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ZoneTopicRepository extends JpaRepository<ZoneTopic, UUID> {
}
