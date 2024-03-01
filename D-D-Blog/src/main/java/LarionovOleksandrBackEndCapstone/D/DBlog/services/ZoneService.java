package LarionovOleksandrBackEndCapstone.D.DBlog.services;



import LarionovOleksandrBackEndCapstone.D.DBlog.entities.Zone;
import LarionovOleksandrBackEndCapstone.D.DBlog.exceptions.NotFoundException;
import LarionovOleksandrBackEndCapstone.D.DBlog.payloads.zone.ZoneDTO;
import LarionovOleksandrBackEndCapstone.D.DBlog.repositories.ZoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ZoneService {
    @Autowired
    private ZoneRepository zoneRepository;
    public Page<Zone> getAllZone(int page, int size, String orderBy) {
        if (size < 0)
            size = 10;
        if (size > 100)
            size = 20;
        Pageable pageable = PageRequest.of(page, size, Sort.by(orderBy));
        return zoneRepository.findAll(pageable);
    }

    public void zoneCreate(ZoneDTO payload){
        Zone newZone = new Zone();
        newZone.setName(payload.name());
        newZone.setOrderNumber(payload.orderNumber());
        zoneRepository.save(newZone);
    }
public Zone findZoneByName(String zoneName){
    return zoneRepository.findByName(zoneName).orElseThrow(() -> new NotFoundException("Zone: " + zoneName + " not found!"));
}
    public Zone findById(UUID id) {
        return zoneRepository.findById(id).orElseThrow(() -> new NotFoundException(String.valueOf(id)));
    }
}
