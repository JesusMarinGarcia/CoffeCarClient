package es.uma.ingweb.coffeecar.controller;


import es.uma.ingweb.coffeecar.consumers.AnnouncementConsumer;
import es.uma.ingweb.coffeecar.entities.Announcement;
import es.uma.ingweb.coffeecar.entities.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.time.LocalDateTime;
import java.util.ArrayList;


@Controller
public class AnnounceControler {

    @PostMapping("/announce/create")
    public String announce(
            @RequestParam(name = "title", required = true) String title,
            @RequestParam(name = "asientos", required = true) Integer seats,
            @RequestParam(name = "link", required = true) String link,
            @RequestParam(name = "descripcion") String desc,
            @RequestParam(name = "latOrigen", required = true) String latDeparture,
            @RequestParam(name = "longOrigen", required = true) String longDeparture,
            @RequestParam(name = "latDestino", required = true) String latArrival,
            @RequestParam(name = "longDestino", required = true) String longArrival,
            @RequestParam(name = "arrival", required = true) String arrival,
            @RequestParam(name = "fechaSalida", required = true) LocalDateTime departureTime,
            @RequestParam(name = "fechaLlegada", required = true) LocalDateTime arrivalTime,
            @SessionAttribute("user") User user
            ){
        AnnouncementConsumer announcementConsumer = new AnnouncementConsumer();
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
        announcement.setDriver(user);
        announcement.setPassengers(new ArrayList<>());
        announcementConsumer.create(announcement);
        return "";
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
