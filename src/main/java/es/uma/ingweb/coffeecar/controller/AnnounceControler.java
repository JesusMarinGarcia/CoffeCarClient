package es.uma.ingweb.coffeecar.controller;


import es.uma.ingweb.coffeecar.consumers.AnnouncementConsumer;
import es.uma.ingweb.coffeecar.entities.Announcement;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(value = "/announce")
@Controller
public class AnnounceControler {

    @PostMapping
    public String announce(){
        AnnouncementConsumer announcementConsumer = new AnnouncementConsumer();
        Announcement announcement = new Announcement();
        return "index";
    }
}
