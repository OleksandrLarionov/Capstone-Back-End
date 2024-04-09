package LarionovOleksandrBackEndCapstone.D.DBlog.beanConfig;


import LarionovOleksandrBackEndCapstone.D.DBlog.entities.User;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static LarionovOleksandrBackEndCapstone.D.DBlog.exceptions.ExceptionsHandler.newDateAndHour;


@Component
public class MailGunSender {

    private String mailGunKey;
    private String mailGunDomain;
    private String myEmail;

    public MailGunSender(@Value("${mailgun.apikey}") String mailGunKey,
                         @Value("${mailgun.domainname}") String mailGunDomain,
                         @Value("${myEmail}") String myEmail) {
        this.myEmail = myEmail;
        this.mailGunKey = mailGunKey;
        this.mailGunDomain = mailGunDomain;
    }

    public void sendMail(String recipient, User user) {
        HttpResponse<JsonNode> response = Unirest.post("https://api.mailgun.net/v3/" + this.mailGunDomain + "/messages")
                .basicAuth("api", this.mailGunKey)
                .queryString("from", "D&D Forum" + this.myEmail)
                .queryString("to", recipient)
                .queryString("subject", "Welcome")
                .queryString("text", "Siamo lieti di informarti che la tua registrazione è avvenuta con successo!" +
                        "\n Username: " + user.getUsername() +
                        "\n Password: " + user.getPassword() +
                        "\n" + "\n" + "\n" + "Se hai domande o dubbi, non esitare a contattare il nostro team di supporto." +
                        "\n Grazie per la tua collaborazione!" +
                        "\n Cordiali Saluti," +
                        "\n D&D STAFF" +
                        "\n" + newDateAndHour())
                .asJson();
    }
}
