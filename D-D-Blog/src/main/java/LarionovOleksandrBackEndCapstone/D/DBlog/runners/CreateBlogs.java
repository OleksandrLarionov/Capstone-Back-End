package LarionovOleksandrBackEndCapstone.D.DBlog.runners;

import LarionovOleksandrBackEndCapstone.D.DBlog.entities.User;
import LarionovOleksandrBackEndCapstone.D.DBlog.entities.ZoneTopic;
import LarionovOleksandrBackEndCapstone.D.DBlog.payloads.BlogPostDTO;
import LarionovOleksandrBackEndCapstone.D.DBlog.repositories.UserRepository;
import LarionovOleksandrBackEndCapstone.D.DBlog.repositories.ZoneTopicRepository;
import LarionovOleksandrBackEndCapstone.D.DBlog.services.BlogPostService;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Order(3)
public class CreateBlogs implements CommandLineRunner {
    @Autowired
    BlogPostService blogPostService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    private ZoneTopicRepository zoneTopicRepository;
    Faker faker = new Faker(new Locale("it"));


    @Override
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        boolean errors = false;
        do {
            System.out.println("Vuoi Procedere Con la Creazione Degli Blogs (y/n)");
            String choice = scanner.nextLine();
            switch (choice.toLowerCase()) {
                case "y" -> {
                    BlogPosts();
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

    public void BlogPosts() {
        for (int i = 0; i < 200; i++) {
            String title = faker.book().title();
            String cover = "no cover";
            String content = "Era una notte tempestosa, e la luna nascondeva timidamente dietro le nubi minacciose. Nel piccolo villaggio di Elmwood, circondato da boschi oscuri e intricati, si narra di una vecchia casa abbandonata, conosciuta solo come la Casa delle Ombre.\n" +
                    "\n" +
                    "Si diceva che la Casa delle Ombre fosse infestata da presenze sinistre e che nessuno osasse avvicinarsi troppo per paura di non tornare più indietro. Ma una giovane coppia, Anne e Mark, avventurieri di cuore, non credevano alle superstizioni. Decisero di sfidare il destino e di esplorare la casa in una notte di luna piena.\n" +
                    "\n" +
                    "Armatisi di torce e coraggio, si avventurarono nelle tenebre della casa. Appena entrarono, sentirono un freddo gelido avvolgere i loro corpi e un senso di oppressione li circondò. Le ombre sembravano danzare lungo le pareti, e strani sussurri riempirono l'aria.\n" +
                    "\n" +
                    "Man mano che esploravano le stanze decrepite, Anne e Mark incontrarono trappole insidiose e illusioni spaventose. Si sentivano sempre più intrappolati nella morsa della Casa delle Ombre, incapaci di trovare la via d'uscita.\n" +
                    "\n" +
                    "Quando finalmente trovarono una porta aperta, si trovarono di fronte a una stanza completamente oscura. Esitanti, entrarono, e all'improvviso la porta si chiuse alle loro spalle con un tonfo sordo. Nel buio totale, sentirono una risata malvagia risuonare intorno a loro, mentre le ombre si avvicinavano sempre di più.\n" +
                    "\n" +
                    "Da quel giorno, Anne e Mark scomparvero misteriosamente, e la Casa delle Ombre continuò a restare un luogo inquietante, dove le anime coraggiose rischiavano di perdersi per sempre tra le sue tenebre.";
            BlogPostDTO newBlogPost = new BlogPostDTO(
                    "Fantasy",
                    title,
                    cover,
                    content,
                    getRandomUserDB().getId(),
                    getRandomZoneTopicFromDb().getId());
            blogPostService.saveBlogPost(newBlogPost);
        }
    }

    public User getRandomUserDB() {
        List<User> userList = userRepository.findAll();
        Random rmd = new Random();
        int rmdIndex = rmd.nextInt(userList.size());
        return userList.get(rmdIndex);
    }

    public ZoneTopic getRandomZoneTopicFromDb() {
        List<ZoneTopic> zoneTopicList = zoneTopicRepository.findAll();
        Random rndm = new Random();
        int rndmIndex = rndm.nextInt(zoneTopicList.size());
        return zoneTopicList.get(rndmIndex);
    }

}
