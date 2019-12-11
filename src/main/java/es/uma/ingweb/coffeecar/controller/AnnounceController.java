package es.uma.ingweb.coffeecar.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.uma.ingweb.coffeecar.consumers.AnnouncementConsumer;
import es.uma.ingweb.coffeecar.consumers.StopConsumer;
import es.uma.ingweb.coffeecar.consumers.UserConsumer;
import es.uma.ingweb.coffeecar.entities.Announce;
import es.uma.ingweb.coffeecar.entities.BusStop;
import es.uma.ingweb.coffeecar.entities.User;
import org.springframework.hateoas.Link;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.URI;
import java.util.List;


@Controller
public class AnnounceController {

    private final AnnouncementConsumer announcementConsumer;
    private final UserConsumer userConsumer;
    private final StopConsumer stopConsumer;

    public AnnounceController(AnnouncementConsumer announcementConsumer, UserConsumer userConsumer, StopConsumer stopConsumer) {
        this.announcementConsumer = announcementConsumer;
        this.userConsumer = userConsumer;
        this.stopConsumer = stopConsumer;
    }

    @PostMapping("createAnnouncement/confirm")
    public String announce(
          @ModelAttribute Announce announce,
          OAuth2AuthenticationToken authenticationToken,
          RedirectAttributes redirectAttrs
    ) {
        User driver = userConsumer.getByEmail(authenticationToken.getPrincipal().getAttribute("email"));
        if (announce.getDescription() == null || announce.getDescription().isEmpty()) {
            announce.setDescription("No hay descripción");
        }

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jsonNodeAnnouncement = objectMapper.valueToTree(announce);
        jsonNodeAnnouncement.put("driver", driver.getLink("self").map(Link::getHref).get());

        URI uri = announcementConsumer.create(jsonNodeAnnouncement);

        Announce announceAux = announcementConsumer.getAnnouncementByURI(uri.toString());

        redirectAttrs
                .addFlashAttribute("mensaje", "Agregado correctamente");

        return "redirect:/details?announcementURI=" + announceAux.getLink("self").map(Link::getHref).get();
    }

    @GetMapping("/createAnnouncement")
    public String createAnnouncement(Model model) {
        model.addAttribute("anuncio", new Announce());
        return "createAnnouncement";
    }

    @GetMapping("/details")
    public String announcementDetails(
          @RequestParam(name = "announcementURI") String uri,
          Model model,
          OAuth2AuthenticationToken authenticationToken) {
        Announce announcement = announcementConsumer.getAnnouncementByURI(uri);
        List<BusStop> stops = stopConsumer
              .getNearby(announcement.getDepartureLatitude(), announcement.getDepartureLongitude());
        User user = userConsumer.getByEmail(authenticationToken.getPrincipal().getAttribute("email"));
        boolean isDriver = announcement.getDriver()
              .equals(user);
        boolean isPassenger = announcement.getPassengers().contains(user);
        boolean canJoin = isPassenger && (announcement.getSeats() > announcement.getPassengers().size());
        model.addAttribute("isDriver", isDriver);
        model.addAttribute("isPassenger", isPassenger);
        model.addAttribute("announcement", announcement);
        model.addAttribute("canJoin",canJoin);
        model.addAttribute("paradas", stops);
        return "announcementDetails";
    }

    @PutMapping("details/join")
    public String joinAnnouncement(
            @ModelAttribute Announce announce,
            OAuth2AuthenticationToken authenticationToken,
            RedirectAttributes redirectAttrs
    ){
        User user = userConsumer.getByEmail(authenticationToken.getPrincipal().getAttribute("email"));
        if(announce.getPassengers().add(user)){
            announcementConsumer.edit(announce);
            redirectAttrs
                    .addFlashAttribute("mensaje", "Te has unido al viaje");
        }else {
            redirectAttrs
                    .addFlashAttribute("mensaje", "No has podido unirte o ya estabas unido");
        }
        return "/";
    }

    @PutMapping("details/left")
    public String leftAnnouncement(
            @ModelAttribute Announce announce,
            OAuth2AuthenticationToken authenticationToken,
            RedirectAttributes redirectAttrs
    ){
        User user = userConsumer.getByEmail(authenticationToken.getPrincipal().getAttribute("email"));
        if(announce.getPassengers().remove(user)){
            announcementConsumer.edit(announce);
            redirectAttrs
                    .addFlashAttribute("mensaje", "Has dejado el viaje");
        }else {
            redirectAttrs
                    .addFlashAttribute("mensaje", "No has podido dejarlo o ya no estabas unido");
        }
        return "/";
    }

    @PostMapping("/announcementDelete")
    public String announcementDelete(
            @RequestParam(name = "announcementURI") String uri,
            RedirectAttributes redirectAttrs) {

        announcementConsumer.delete(uri);
        redirectAttrs
                .addFlashAttribute("mensaje", "Eliminado correctamente");
        return "redirect:/";
    }
}
