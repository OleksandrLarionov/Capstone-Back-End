package LarionovOleksandrBackEndCapstone.D.DBlog.runners;

import LarionovOleksandrBackEndCapstone.D.DBlog.entities.ZoneTopic;
import LarionovOleksandrBackEndCapstone.D.DBlog.payloads.zone.ZoneDTO;
import LarionovOleksandrBackEndCapstone.D.DBlog.payloads.zone.ZoneTopicDTO;
import LarionovOleksandrBackEndCapstone.D.DBlog.services.ZoneService;
import LarionovOleksandrBackEndCapstone.D.DBlog.services.ZoneTopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
@Order(4)
public class CreateZoneAndTopics implements CommandLineRunner {
    @Autowired
    private ZoneService zoneService;
    @Autowired
    private ZoneTopicService zoneTopicService;
    @Override
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        boolean errors = false;
        do {
            System.out.println("Vuoi Procedere Con la Creazione Delle zone (y/n)");
            String choice = scanner.nextLine();
            switch (choice.toLowerCase()) {
                case "y" -> {
                    createZones();
                    errors = false;
                }
                case "n" -> errors = false;
                default -> {
                    System.out.println("Input non valido. Riprova.");
                    errors = true;
                }
            }
        } while (errors);
    }

    public void createZones(){
        ZoneDTO welcome = new ZoneDTO("Welcome",1);
        ZoneDTO dAndD = new ZoneDTO("D&D",2);
        ZoneDTO offTopic = new ZoneDTO("off Topic",3);

        zoneService.zoneCreate(welcome);
        zoneService.zoneCreate(dAndD);
        zoneService.zoneCreate(offTopic);

        ZoneTopicDTO welcomeTopic = new ZoneTopicDTO("Welcome","Benvenuto, presentati in questa gilda per trovare i nuovi compagni d'armi", zoneService.findZoneByName("Welcome").getId());
        ZoneTopicDTO update = new ZoneTopicDTO("Updates","Tutte le novita sul mondo D&D", zoneService.findZoneByName("Welcome").getId());
        ZoneTopicDTO support = new ZoneTopicDTO("Supporto","Ti serve aiuto? segnala il problema! nessuno farà la spia.", zoneService.findZoneByName("Welcome").getId());

        ZoneTopicDTO introduction = new ZoneTopicDTO("Introduzione","Introduzione Sul Mondo D&D", zoneService.findZoneByName("D&D").getId());
        ZoneTopicDTO characters = new ZoneTopicDTO("Personaggi","Sei un novizio? Vieni qui e diventerai ciò che vuoi...",zoneService.findZoneByName("D&D").getId());
        ZoneTopicDTO schede = new ZoneTopicDTO("Scheda Giocatore","Qui avrai il tuo attestato per la Campagna!", zoneService.findZoneByName("D&D").getId());
        ZoneTopicDTO adventure = new ZoneTopicDTO("Avventura","Giunta l'ora di scegliere la missione...", zoneService.findZoneByName("D&D").getId());

        ZoneTopicDTO stories = new ZoneTopicDTO("Storie","Raccontaci delle tue avventure", zoneService.findZoneByName("off Topic").getId());
        ZoneTopicDTO funny = new ZoneTopicDTO("Scene Divertenti","Qualcosa di divertente capita spesso nelle missioni pericolose e non solo...", zoneService.findZoneByName("off Topic").getId());
        ZoneTopicDTO epiche = new ZoneTopicDTO("Scene Epiche","Hai portato la missione a termine? I nostri bardi pensaranno a cantare e raccontare le tue azioni epiche.", zoneService.findZoneByName("off Topic").getId());

        zoneTopicService.zoneTopicCreate(welcomeTopic);
        zoneTopicService.zoneTopicCreate(update);
        zoneTopicService.zoneTopicCreate(support);
        zoneTopicService.zoneTopicCreate(introduction);
        zoneTopicService.zoneTopicCreate(characters);
        zoneTopicService.zoneTopicCreate(schede);
        zoneTopicService.zoneTopicCreate(adventure);
        zoneTopicService.zoneTopicCreate(stories);
        zoneTopicService.zoneTopicCreate(funny);
        zoneTopicService.zoneTopicCreate(epiche);

    }
}
