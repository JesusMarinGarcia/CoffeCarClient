package es.uma.ingweb.coffeecar.controller;

import es.uma.ingweb.coffeecar.consumers.AnnouncementConsumer;
import es.uma.ingweb.coffeecar.consumers.UserConsumer;
import es.uma.ingweb.coffeecar.entities.User;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Objects;

@Controller
public class HomePageController {
    private final UserConsumer userConsumer;
    private final AnnouncementConsumer announcementConsumer;

    public HomePageController(AnnouncementConsumer announcementConsumer, UserConsumer userConsumer) {
        this.userConsumer = userConsumer;
        this.announcementConsumer = announcementConsumer;
    }

    @GetMapping("/")
    public String home(OAuth2AuthenticationToken authenticationToken, Model model) {
        String email = authenticationToken.getPrincipal().getAttribute("email");
        String name = authenticationToken.getPrincipal().getAttribute("name");

        User user = createIfDoesntExist(email, name);

        model.addAttribute("announcementsAvailable", announcementConsumer.getAll());
        model.addAttribute("myTrips", announcementConsumer.getMyTrips(email));

        return "home";
    }

    private User createIfDoesntExist(String email, String name) {
        return userConsumer.optionalGetByEmail(email)
              .filter(u -> Objects.nonNull(u.getEmail()))
              .orElseGet(() -> createUser(email, name));
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
