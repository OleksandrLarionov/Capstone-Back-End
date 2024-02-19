package LarionovOleksandrBackEndCapstone.D.DBlog.services;

import LarionovOleksandrBackEndCapstone.D.DBlog.entities.Zone;
import LarionovOleksandrBackEndCapstone.D.DBlog.entities.ZoneTopic;
import LarionovOleksandrBackEndCapstone.D.DBlog.payloads.zone.ZoneTopicDTO;
import LarionovOleksandrBackEndCapstone.D.DBlog.repositories.ZoneTopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ZoneTopicService {
    @Autowired
    private ZoneTopicRepository zoneTopicRepository;
    @Autowired
    private ZoneService zoneService;
    public Page<ZoneTopic> getAllZoneTopics(int page, int size, String orderBy) {
        if (size < 0)
            size = 10;
        if (size > 100)
            size = 20;
        Pageable pageable = PageRequest.of(page, size, Sort.by(orderBy));
        return zoneTopicRepository.findAll(pageable);
    }

public void zoneTopicCreate(ZoneTopicDTO payload){
ZoneTopic newZoneTopic = new ZoneTopic();
newZoneTopic.setName(payload.name());
newZoneTopic.setDescription(payload.description());
Zone foundZone = zoneService.findById(payload.zoneId());
newZoneTopic.setZone(foundZone);
zoneTopicRepository.save(newZoneTopic);
}
}
