package LarionovOleksandrBackEndCapstone.D.DBlog.payloads.zone;

import LarionovOleksandrBackEndCapstone.D.DBlog.entities.Zone;

import java.util.UUID;

public record ZoneTopicDTO(
        String name,
        String description,
        UUID zoneId) {
}
