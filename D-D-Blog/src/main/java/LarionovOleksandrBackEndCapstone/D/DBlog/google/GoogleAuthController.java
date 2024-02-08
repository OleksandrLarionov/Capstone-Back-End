package LarionovOleksandrBackEndCapstone.D.DBlog.google;

import LarionovOleksandrBackEndCapstone.D.DBlog.entities.User;
import LarionovOleksandrBackEndCapstone.D.DBlog.exceptions.NotFoundException;
import LarionovOleksandrBackEndCapstone.D.DBlog.payloads.AutPayload;
import LarionovOleksandrBackEndCapstone.D.DBlog.payloads.user.NewUserDTO;
import LarionovOleksandrBackEndCapstone.D.DBlog.security.JWTTools;
import LarionovOleksandrBackEndCapstone.D.DBlog.services.AuthService;
import LarionovOleksandrBackEndCapstone.D.DBlog.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/google")
public class GoogleAuthController {

    @Autowired
    private GoogleAuthService googleAuthService;

    @Autowired
    private UserService userService;
    @Autowired
    private AuthService authService;
    @Autowired
    private JWTTools jwtTools;


    @GetMapping("/callback")
    public void googleCallback(@RequestParam("code") String authorizationCode) throws NotFoundException {

        GoogleAccessTokenResponse accessTokenResponse = googleAuthService.getAccessToken(authorizationCode);
        String accessToken = accessTokenResponse.getAccess_token();

        GoogleUserInfoResponse userInfoResponse = googleAuthService.getUserInfo(accessToken);
        String email = userInfoResponse.getEmail();
        String username = userInfoResponse.getName();
        String firstname = userInfoResponse.getGiven_name();
        String lastname = userInfoResponse.getFamily_name();
        String profileImgUrl = userInfoResponse.getPicture();

        User user = null;

        try {
            user = userService.findByEmail(email);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        if (user == null) {
            NewUserDTO newUser = new NewUserDTO();
            newUser.setEmail(email);
            newUser.setUsername(username);
            newUser.setName(firstname);
            newUser.setSurname(lastname);
            newUser.setProfileImg(profileImgUrl);
            authService.saveNewUser(newUser);

            user = userService.findByEmail(email);
        }
        if (user != null) {
            String token = jwtTools.createToken(user);
            addAccessTokenToCookie(token);
//            getAccessTokenFromCookie();

        }}
        @GetMapping("/authorization-url")
        public ResponseEntity<String> getGoogleAuthorizationUrl () {
            return new ResponseEntity<>(googleAuthService.getAuthorizationUrl(), HttpStatus.OK);

        }

    public ResponseEntity<?> addAccessTokenToCookie(String accessToken) {
        Cookie cookie = new Cookie("access_token", accessToken);
        cookie.setPath("/");
        cookie.setMaxAge(3600); // Tempo di scadenza del cookie in secondi (1 ora)
        cookie.setHttpOnly(true); // Il cookie sarà accessibile solo da codice server-side

        // Crea una ResponseEntity vuota con lo stato HTTP 200 (OK) e aggiungi il cookie all'header
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, cookie.toString());
        return new ResponseEntity<>(null, headers, HttpStatus.OK);
    }
    public String getAccessTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("access_token")) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }


    }
