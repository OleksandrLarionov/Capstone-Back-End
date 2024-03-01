package LarionovOleksandrBackEndCapstone.D.DBlog.runners;

import LarionovOleksandrBackEndCapstone.D.DBlog.entities.User;
import LarionovOleksandrBackEndCapstone.D.DBlog.entities.ZoneTopic;
import LarionovOleksandrBackEndCapstone.D.DBlog.payloads.BlogPostDTO;
import LarionovOleksandrBackEndCapstone.D.DBlog.repositories.UserRepository;
import LarionovOleksandrBackEndCapstone.D.DBlog.repositories.ZoneTopicRepository;
import LarionovOleksandrBackEndCapstone.D.DBlog.services.BlogPostService;
import LarionovOleksandrBackEndCapstone.D.DBlog.services.ZoneTopicService;
import com.github.javafaker.Faker;
import com.google.gson.stream.JsonReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static com.google.gson.stream.JsonToken.BEGIN_ARRAY;

@Component
@Order(3)
public class CreateBlogs implements CommandLineRunner {
    @Autowired
    BlogPostService blogPostService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    private ZoneTopicRepository zoneTopicRepository;
    @Autowired
    ZoneTopicService zoneTopicService;
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
//                    BlogPosts();
                    Content();
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

    public void Content() {
        String nomeFile = "src/main/resources/forum_content.json";

        try (JsonReader reader = new JsonReader(new FileReader(nomeFile))) {
            reader.beginObject(); // Inizio dell'oggetto JSON

            Map<String, List<BlogPostDTO>> dataMap = new HashMap<>();

            while (reader.hasNext()) {
                String categoryName = reader.nextName();
                if (reader.peek() == BEGIN_ARRAY) {
                    reader.beginArray();

                    List<BlogPostDTO> posts = new ArrayList<>();

                    while (reader.hasNext()) {
                        reader.beginObject();

                        String category = null;
                        String title = null;
                        String cover = null;
                        String content = null;

                        while (reader.hasNext()) {
                            String name = reader.nextName();
                            if (name.equals("category")) {
                                category = reader.nextString();
                            } else if (name.equals("title")) {
                                title = reader.nextString();
                            } else if (name.equals("cover")) {
                                cover = reader.nextString();
                            } else if (name.equals("content")) {
                                content = reader.nextString();
                            } else {
                                reader.skipValue();
                            }
                        }

                        reader.endObject();

                        // Creazione di un oggetto BlogPostDTO e salvataggio nella mappa
                        if (category != null && title != null && content != null) {
                            posts.add(new BlogPostDTO(category, title, cover, content, getRandomUserDB().getId(), zoneTopicService.findZoneTopicByName(categoryName).getId()));
                        }
                    }

                    dataMap.put(categoryName, posts);
                    reader.endArray(); // Fine lettura
                } else {
                    reader.skipValue();
                }
            }

            reader.endObject(); // Fine JSON

            // Stampa e salvataggio dei dati
            for (Map.Entry<String, List<BlogPostDTO>> entry : dataMap.entrySet()) {
                String category = entry.getKey();
                List<BlogPostDTO> posts = entry.getValue();

                System.out.println("Categoria: " + category);
                for (BlogPostDTO post : posts) {
                 blogPostService.saveBlogPost(post);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
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
