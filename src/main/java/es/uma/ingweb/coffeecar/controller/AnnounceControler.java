package es.uma.ingweb.coffeecar.controller;


import es.uma.ingweb.coffeecar.consumers.AnnouncementConsumer;
import es.uma.ingweb.coffeecar.consumers.UserConsumer;
import es.uma.ingweb.coffeecar.entities.Announcement;
import es.uma.ingweb.coffeecar.entities.User;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
            @ModelAttribute Announcement announcement,
            OAuth2AuthenticationToken authenticationToken,
            RedirectAttributes redirectAttrs/*,
            @RequestParam (name = "fechaSalida") String fsalida,
            @RequestParam (name = "fechaLlegada") String fllegada*/
            ){
        User driver =  userConsumer.getByEmail(authenticationToken.getPrincipal().getAttribute("email"));
        if (announcement.getDescription() == null || announcement.getDescription().isEmpty()){
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
