package es.uma.ingweb.coffeecar.controller;

import es.uma.ingweb.coffeecar.consumers.AnnouncementConsumer;
import es.uma.ingweb.coffeecar.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.List;

@Controller
public class HomePageController {
    @Autowired
    private AnnouncementConsumer announcementConsumer;

    @GetMapping("/home")
    public String home(OAuth2AuthenticationToken token, Model model ){
        model.addAttribute("availableAnnouncements", announcementConsumer.getAvailableAnnouncements(token.getPrincipal().getAttribute("email")));
        model.addAttribute("myTrips", announcementConsumer.getMyTrips(token.getPrincipal().getAttribute("email")));
        return "/home";
    }
}
