package es.uma.ingweb.coffeecar.controller;

import es.uma.ingweb.coffeecar.consumers.AnnouncementConsumer;
import es.uma.ingweb.coffeecar.consumers.BusConsumer;
import es.uma.ingweb.coffeecar.consumers.StopConsumer;
import es.uma.ingweb.coffeecar.consumers.UserConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ClientController {

    @Autowired
    private AnnouncementConsumer announcementConsumer;
    @Autowired
    private UserConsumer userConsumer;
    @Autowired
    private BusConsumer busConsumer;
    @Autowired
    private StopConsumer stopConsumer;

    @GetMapping("/index")
    public String index(){
        return "index";
    }

    @GetMapping("/createAnnouncement")
    public String createAnnouncement(){
        return "createAnnouncement";
    }

    @GetMapping("/announcementDetails")
    public String announcementDetails(){
        return "announcementDetails";
    }
}
