package es.uma.ingweb.coffeecar.controller;


import es.uma.ingweb.coffeecar.consumers.AnnouncementConsumer;
import es.uma.ingweb.coffeecar.consumers.UserConsumer;
import es.uma.ingweb.coffeecar.entities.Announcement;
import es.uma.ingweb.coffeecar.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


@Controller
public class AnnounceControler {

    final
    RestTemplate restTemplate;

    public AnnounceControler(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostMapping("/announce/create")
    public String announce(
            @ModelAttribute Announcement announcement,
            OAuth2AuthenticationToken authenticationToken,
            RedirectAttributes redirectAttrs/*,
            @RequestParam (name = "fechaSalida") String fsalida,
            @RequestParam (name = "fechaLlegada") String fllegada*/
            ){
        AnnouncementConsumer announcementConsumer = new AnnouncementConsumer(restTemplate);
        Announcement announcement = new Announcement();
        announcement.setArrival(arrival);
        announcement.setTitle(title);
        announcement.setSeats(seats);
        announcement.setImgLink(link);
        if (desc == null || desc.isEmpty()){
            announcement.setDescription("No hay descripción");
        }
        announcement.setDriver(driver);
        announcement.setPassengers(new ArrayList<>());
        /*DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime departureDate = LocalDateTime.parse(fsalida, formatter);
        LocalDateTime arrivalDate = LocalDateTime.parse(fllegada, formatter);
        announcement.setDepartureTime(departureDate);
        announcement.setArrivalDate(arrivalDate);*/

        announcementConsumer.create(announcement);
        redirectAttrs
                .addFlashAttribute("mensaje", "Agregado correctamente");
        return "redirect:/";
    }

    @GetMapping("/createAnnouncement")
    public String createAnnouncement(Model model){
        model.addAttribute("anuncio",new Announcement());
        return "createAnnouncement";
    }

    @GetMapping("/announcementDetails")
    public String announcementDetails(@RequestParam(name="announcementId") long id){
        return "announcementDetails";
    }
}
