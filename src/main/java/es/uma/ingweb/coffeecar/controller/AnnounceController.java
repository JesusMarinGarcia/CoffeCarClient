package es.uma.ingweb.coffeecar.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.uma.ingweb.coffeecar.consumers.AnnouncementConsumer;
import es.uma.ingweb.coffeecar.consumers.UserConsumer;
import es.uma.ingweb.coffeecar.entities.Announce;
import es.uma.ingweb.coffeecar.entities.User;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class AnnounceController {

    private final AnnouncementConsumer announcementConsumer;
    private final UserConsumer userConsumer;

    public AnnounceController(AnnouncementConsumer announcementConsumer, UserConsumer userConsumer) {
        this.announcementConsumer = announcementConsumer;
        this.userConsumer = userConsumer;
    }

    @PostMapping("createAnnouncement/confirm")
    public String announce(
          @ModelAttribute Announce announce,
          OAuth2AuthenticationToken authenticationToken,
          RedirectAttributes redirectAttrs/*,
            @RequestParam (name = "fechaSalida") String fsalida,
            @RequestParam (name = "fechaLlegada") String fllegada*/
    ) {
        User driver = userConsumer.getByEmail(authenticationToken.getPrincipal().getAttribute("email"));
        if (announce.getDescription() == null || announce.getDescription().isEmpty()) {
            announce.setDescription("No hay descripción");
        }
        /*DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime departureDate = LocalDateTime.parse(fsalida, formatter);
        LocalDateTime arrivalDate = LocalDateTime.parse(fllegada, formatter);
        announcement.setDepartureTime(departureDate);
        announcement.setArrivalDate(arrivalDate);*/

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jsonNodeAnnouncement = objectMapper.valueToTree(announce);
        jsonNodeAnnouncement.put("driver", driver.getSelfURI());

        announce.setSelfURI(announcementConsumer.create(jsonNodeAnnouncement));

        redirectAttrs
                .addFlashAttribute("mensaje", "Agregado correctamente");
        return "redirect:/";
    }

    @GetMapping("/createAnnouncement")
    public String createAnnouncement(Model model){
        model.addAttribute("anuncio", new Announce());
        return "createAnnouncement";
    }

    @GetMapping("/announcementDetails")
    public String announcementDetails(@RequestParam(name="announcementURI") String URI, Model model){
        model.addAttribute("announcement", announcementConsumer.getAnnouncementByURI(URI));
        return "announcementDetails";
    }
}
