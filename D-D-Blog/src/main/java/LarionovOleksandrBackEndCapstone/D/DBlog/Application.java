package LarionovOleksandrBackEndCapstone.D.DBlog;

import LarionovOleksandrBackEndCapstone.D.DBlog.runners.CreateBlogs;
import LarionovOleksandrBackEndCapstone.D.DBlog.runners.CreateComments;
import LarionovOleksandrBackEndCapstone.D.DBlog.runners.CreateUsers;
import LarionovOleksandrBackEndCapstone.D.DBlog.runners.CreateZoneAndTopics;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
