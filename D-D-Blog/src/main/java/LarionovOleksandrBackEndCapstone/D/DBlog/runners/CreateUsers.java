package LarionovOleksandrBackEndCapstone.D.DBlog.runners;


import LarionovOleksandrBackEndCapstone.D.DBlog.ENUMS.ROLE;
import LarionovOleksandrBackEndCapstone.D.DBlog.entities.User;
import LarionovOleksandrBackEndCapstone.D.DBlog.payloads.user.NewUserDTO;
import LarionovOleksandrBackEndCapstone.D.DBlog.repositories.UserRepository;
import LarionovOleksandrBackEndCapstone.D.DBlog.services.AuthService;
import LarionovOleksandrBackEndCapstone.D.DBlog.services.UserService;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

@Component
@Order(1)
public class CreateUsers implements CommandLineRunner {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthService authService;
    @Autowired
    private UserRepository userRepository;
    Faker faker = new Faker(new Locale("it"));
    @Value("${my.email}")
    private String email;

    @Override
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        boolean errors = false;
        do {
            System.out.println("Vuoi Procedere Con la Creazione Degli Utenti (y/n)");
            String choice = scanner.nextLine();
            switch (choice.toLowerCase()) {
                case "y" -> {
                    admin();
                    users();
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

    public void admin() {
        NewUserDTO newUser = new NewUserDTO();
        newUser.setUsername("Stein");
        newUser.setName("Alex");
        newUser.setSurname("Larionov");
        newUser.setEmail(email);
        newUser.setPassword("lollipoP1%");
        newUser.setSecretAnswer("peppapeppapeppa");
        newUser.setBDay(LocalDate.of(1991, 5, 23));
        authService.saveNewUser(newUser);
        User user = userService.findByEmail(email);
        user.setRole(ROLE.ADMIN);
        userRepository.save(user);
    }
    public void users(){
        for (int i = 0; i < 30; i++){
            String name = faker.funnyName().name();
            String surname = faker.name().lastName();
            String username = faker.name().username();
            String email = faker.internet().emailAddress();
            String password = faker.internet().password();
            String secret = faker.artist().name();
            NewUserDTO newUser = new NewUserDTO();
            newUser.setUsername(username);
            newUser.setName(name);
            newUser.setSurname(surname);
            newUser.setEmail(email);
            newUser.setPassword(password);
            newUser.setSecretAnswer(secret + secret);
            newUser.setBDay(randomDate());
            authService.saveNewUser(newUser);
        }
    }
    public static LocalDate randomDate() {
        long minDay = LocalDate.of(1980, 1, 1).toEpochDay();
        long maxDay = LocalDate.of(2015, 12, 31).toEpochDay();
        long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
        return LocalDate.ofEpochDay(randomDay);
    }
}
