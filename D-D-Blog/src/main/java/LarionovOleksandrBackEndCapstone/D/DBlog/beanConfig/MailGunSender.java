package LarionovOleksandrBackEndCapstone.D.DBlog.beanConfig;



import LarionovOleksandrBackEndCapstone.D.DBlog.payloads.user.NewUserDTO;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;



@Component
public class MailGunSender {

    private String mailGunKey;
    private String mailGunDomain;
    private String myEmail;


    public MailGunSender(@Value("${API_MAIL_KEY}") String mailGunKey,
                         @Value("${MAILGUN_DOMAIN_MAIL}") String mailGunDomain,
                         @Value("${MY_EMAIL}") String myEmail) {
        this.myEmail = myEmail;
        this.mailGunKey = mailGunKey;
        this.mailGunDomain = mailGunDomain;
    }

    public void sendMail(String recipient, NewUserDTO payload, String token) {
        String url = "http://localhost:3001/auth/register/confirm/" + token;
        // Configura il risolutore del template
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("templates/"); // Imposta la directory dei  template HTML
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);

        // Crea il motore del template
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        Context context = new Context();
        context.setVariable("name", payload.getName());
        context.setVariable("email", payload.getEmail()); // variabili all'interno del template
        context.setVariable("password", payload.getPassword());
        context.setVariable("url", url);

        // Processa il template  per ottenere il corpo dell'email HTML
        String htmlContent  = templateEngine.process("registration", context);

        HttpResponse<JsonNode> response = Unirest.post("https://api.mailgun.net/v3/" + this.mailGunDomain + "/messages")
                .basicAuth("api", this.mailGunKey)
                .queryString("from", "D&D Forum" + this.myEmail)
                .queryString("to", recipient)
                .queryString("subject", "Welcome")
                .queryString("text", "Test sending registration mail")
                .field("html", htmlContent )
                .asJson();
    }
}
