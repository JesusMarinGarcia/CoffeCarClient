package es.uma.ingweb.coffeecar.controller;

import es.uma.ingweb.coffeecar.consumers.AnnouncementConsumer;
import es.uma.ingweb.coffeecar.consumers.UserConsumer;
import es.uma.ingweb.coffeecar.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Objects;

@Controller
public class HomePageController {
    private final AnnouncementConsumer announcementConsumer;
    private final UserConsumer userConsumer;

    public HomePageController(AnnouncementConsumer announcementConsumer, UserConsumer userConsumer) {
        this.announcementConsumer = announcementConsumer;
        this.userConsumer = userConsumer;
    }

    @GetMapping("/")
    public String home(OAuth2AuthenticationToken authenticationToken, Model model) {
        String email = authenticationToken.getPrincipal().getAttribute("email");
        String name = authenticationToken.getPrincipal().getAttribute("name");
        User user = userConsumer.optionalGetByEmail(email)
              .filter(u -> Objects.nonNull(u.getEmail()))
              .orElseGet(() -> createUser(email, name));
        model.addAttribute("announcements", announcementConsumer.getAvailableAnnouncements(email));

        return "home";
    }

    @GetMapping("/")
    public String availableAnnouncements(OAuth2AuthenticationToken authenticationToken, Model model){
        model.addAttribute("announcements", announcementConsumer.getAvailableAnnouncements(authenticationToken
                .getPrincipal()
                .getAttribute("email")));
        return "announcementsTable :: resultAnnouncements";
    }

    @GetMapping("/myTrips")
    public String showMyTrips(OAuth2AuthenticationToken authenticationToken, Model model){
        model.addAttribute("announcements", announcementConsumer.getMyTrips(authenticationToken
                                                                                    .getPrincipal()
                                                                                    .getAttribute("email")));
        return "announcementsTable :: resultAnnouncements";
    }

    private User createUser(String email, String name) {
        User user = User.builder()
              .email(email)
              .name(name)
              .build();
        userConsumer.create(user);
        return user;
    }
}
