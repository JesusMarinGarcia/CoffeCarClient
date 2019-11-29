package es.uma.ingweb.coffeecar.controller;


import es.uma.ingweb.coffeecar.consumers.AnnouncementConsumer;
import es.uma.ingweb.coffeecar.entities.Announcement;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;


@Controller
public class AnnounceControler {

    @PostMapping("/announce/create")
    public String announce(
            @RequestParam(name = "title") String title,
            @RequestParam(name = "asientos") Integer seats,
            @RequestParam(name = "link") String link,
            @RequestParam(name = "descripcion") String desc,
            @RequestParam(name = "latOrigen") String latDeparture,
            @RequestParam(name = "longOrigen") String longDeparture,
            @RequestParam(name = "latDestino") String latArrival,
            @RequestParam(name = "longDestino") String longArrival,
            @RequestParam(name = "arrival") String arrival,
            @RequestParam(name = "fechaSalida") LocalDateTime departureTime,
            @RequestParam(name = "fechaLlegada") LocalDateTime arrivalTime
            ){
        AnnouncementConsumer announcementConsumer = new AnnouncementConsumer();
        Announcement announcement = new Announcement();
        announcement.setArrival(arrival);
        announcement.setTitle(title);
        announcement.setSeats(seats);
        announcement.setImgLink(link);
        announcement.setDescription(desc);
        announcement.setDepartureLatitude(Double.parseDouble(latDeparture));
        announcement.setGetDepartureLongitude(Double.parseDouble(longDeparture));
        announcement.setArrivalLatitude(Double.parseDouble(latArrival));
        announcement.setArrivalLongitude(Double.parseDouble(longArrival));
        announcement.setDepartureTime(departureTime);
        announcement.setArrivalDate(arrivalTime);
        announcementConsumer.create(announcement);
        return "index";
    }
}
