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
            ){
        User driver = userConsumer.getByEmail(authenticationToken.getPrincipal().getAttribute("email"));
        if (announce.getDescription() == null || announce.getDescription().isEmpty()) {
            announce.setDescription("No hay descripción");
        }

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jsonNodeAnnouncement = objectMapper.valueToTree(announce);
        jsonNodeAnnouncement.put("driver", driver.getLink("self").map(Link::getHref).get());

        announcementConsumer.create(jsonNodeAnnouncement);

        redirectAttrs
                .addFlashAttribute("mensaje", "Agregado correctamente");
        return "redirect:/";
    }

    @GetMapping("/createAnnouncement")
    public String createAnnouncement(Model model){
        model.addAttribute("anuncio", new Announce());
        return "createAnnouncement";
    }

    @GetMapping("/details")
    public String announcementDetails(
            @RequestParam(name="announcementURI") String uri,
            Model model,
            OAuth2AuthenticationToken authenticationToken){
        Announce announcement = announcementConsumer.getAnnouncementByURI(uri);

        List<BusStop> stops = stopConsumer.getNearby((float)announcement.getDepartureLatitude(),(float)announcement.getDepartureLongitude());
        User user = userConsumer.getByEmail(authenticationToken.getPrincipal().getAttribute("email"));
        boolean isDriver = announcement.getDriver()
                .equals(user);
        boolean isPassenger = announcement.getPassengers().contains(user);
        model.addAttribute("isDriver", isDriver);
        model.addAttribute("isPassenger", isPassenger);
        model.addAttribute("announcement", announcement);
        model.addAttribute("paradas", stops);
        return "announcementDetails";
    }

    @GetMapping("/announcementDelete")
    public String announcementDelete(
            @ModelAttribute Announce announcement,
            OAuth2AuthenticationToken authenticationToken,
            RedirectAttributes redirectAttrs){
        User driver =  userConsumer.getByEmail(authenticationToken.getPrincipal().getAttribute("email"));
        if(announcement.getDriver().equals(driver)){
            announcementConsumer.delete(announcement);
            redirectAttrs
                    .addFlashAttribute("mensaje", "Eliminado correctamente");
        }else{
            redirectAttrs
                    .addFlashAttribute("mensaje", "No tienes permiso para esta acción");
        }
        return "redirect:/";
    }

    // metodo al querer editar un anuncio
    @GetMapping("/editarAnuncio")
    public String editAnnouncement(@RequestParam(name="announcementURI") String uri,
                                   Model model){
            System.out.println(uri);
            Announce announce = announcementConsumer.getAnnouncementByURI(uri);
            model.addAttribute("announce", announce);
            model.addAttribute("uri",uri);
        return "editAnnouncement";
    }
    //metodo cuando se modifica el anuncio
    @GetMapping("/editarAnuncio/confirm")
    public String changeAnnouncement(@ModelAttribute Announce announce,@RequestParam("UriAnnounce") String uri, Model model,
                                     OAuth2AuthenticationToken authenticationToken, RedirectAttributes redirectAttrs){
        if (announce.getDescription() == null || announce.getDescription().isEmpty()) {
            announce.setDescription("No hay descripción");
        }
        Announce announce1 = announcementConsumer.getAnnouncementByURI(uri);
        announce.add(announce1.getLinks());
        announcementConsumer.edit(announce);


    /*
        List<BusStop> stops = stopConsumer
                .getNearby((float)announcement.getDepartureLatitude(),(float)announcement.getDepartureLongitude());
        User user = userConsumer.getByEmail(authenticationToken.getPrincipal().getAttribute("email"));
        boolean isDriver = announcement.getDriver()
                .equals(user);
        boolean isPassenger = announcement.getPassengers().contains(user);
        model.addAttribute("isDriver", isDriver);
        model.addAttribute("isPassenger", isPassenger);
        model.addAttribute("announcement", announcement);
        model.addAttribute("paradas", stops);*/
        redirectAttrs
                .addFlashAttribute("mensaje", "Agregado correctamente");
        return "redirect:/";
    }
}
