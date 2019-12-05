package es.uma.ingweb.coffeecar.controller;


import es.uma.ingweb.coffeecar.consumers.AnnouncementConsumer;
import es.uma.ingweb.coffeecar.consumers.UserConsumer;
import es.uma.ingweb.coffeecar.entities.Announcement;
import es.uma.ingweb.coffeecar.entities.User;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.ArrayList;


@Controller
public class AnnounceControler {

    private final AnnouncementConsumer announcementConsumer;
    private final UserConsumer userConsumer;

    public AnnounceControler(AnnouncementConsumer announcementConsumer, UserConsumer userConsumer) {
        this.announcementConsumer = announcementConsumer;
        this.userConsumer = userConsumer;
    }

    @PostMapping("createAnnouncement/confirm")
    public String announce(
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "asientos", required = false) Integer seats,
            @RequestParam(name = "link", required = false) String link,
            @RequestParam(name = "descripcion", required = false) String desc,
            @RequestParam(name = "latOrigen", required = false) String latDeparture,
            @RequestParam(name = "longOrigen", required = false) String longDeparture,
            @RequestParam(name = "latDestino", required = false) String latArrival,
            @RequestParam(name = "longDestino", required = false) String longArrival,
            @RequestParam(name = "arrival", required = false) String arrival,
            @RequestParam(name = "fechaSalida", required = false) LocalDateTime departureTime,
            @RequestParam(name = "fechaLlegada", required = false) LocalDateTime arrivalTime,
            OAuth2AuthenticationToken authenticationToken
            ){
        Announcement announcement = new Announcement();
        announcement.setArrival(arrival);
        announcement.setTitle(title);
        announcement.setSeats(seats);
        announcement.setImgLink(link);
        if (desc == null || desc.isEmpty()){
            announcement.setDescription("No hay descripción");
        }else{
            announcement.setDescription(desc);
        }
        announcement.setDepartureLatitude(Double.parseDouble(latDeparture));
        announcement.setGetDepartureLongitude(Double.parseDouble(longDeparture));
        announcement.setArrivalLatitude(Double.parseDouble(latArrival));
        announcement.setArrivalLongitude(Double.parseDouble(longArrival));
        announcement.setDepartureTime(departureTime);
        announcement.setArrivalDate(arrivalTime);
        announcement.setDriver(userConsumer.getByEmail(authenticationToken.getPrincipal().getAttribute("email")));
        announcement.setPassengers(new ArrayList<>());
        announcementConsumer.create(announcement);
        return "/home";
    }

    @GetMapping("/createAnnouncement")
    public String createAnnouncement(){
        return "createAnnouncement";
    }

    @GetMapping("/announcementDetails")
    public String announcementDetails(@RequestParam(name="announcementId") long id){
        return "announcementDetails";
    }
}
