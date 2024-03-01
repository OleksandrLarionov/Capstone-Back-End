package LarionovOleksandrBackEndCapstone.D.DBlog.repositories;

import LarionovOleksandrBackEndCapstone.D.DBlog.entities.Zone;
import LarionovOleksandrBackEndCapstone.D.DBlog.entities.ZoneTopic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ZoneTopicRepository extends JpaRepository<ZoneTopic, UUID> {
    Optional<ZoneTopic> findByName(String zoneTopicName);
}
