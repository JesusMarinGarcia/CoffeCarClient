package es.uma.ingweb.coffeecar.controller;

import es.uma.ingweb.coffeecar.consumers.AnnouncementConsumer;
import es.uma.ingweb.coffeecar.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/")
    public String home(@SessionAttribute("user") User user, Model model ){
        model.addAttribute("availableAnnouncements", announcementConsumer.getAvailableAnnouncements(user.getMail()));
        model.addAttribute("myTrips", announcementConsumer.getMyTrips(user.getMail()));
        return "/home";
    }
}
