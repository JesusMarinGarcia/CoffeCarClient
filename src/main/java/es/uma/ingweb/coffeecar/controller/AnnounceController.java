package es.uma.ingweb.coffeecar.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.uma.ingweb.coffeecar.consumers.AnnouncementConsumer;
import es.uma.ingweb.coffeecar.consumers.UserConsumer;
import es.uma.ingweb.coffeecar.entities.Announcement;
import es.uma.ingweb.coffeecar.entities.User;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkRelation;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;


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
            @ModelAttribute Announcement announcement,
            OAuth2AuthenticationToken authenticationToken,
            RedirectAttributes redirectAttrs
            ){
        User driver = userConsumer.getByEmail(authenticationToken.getPrincipal().getAttribute("email"));
        if (announcement.getDescription() == null || announcement.getDescription().isEmpty()){
            announcement.setDescription("No hay descripción");
        }

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jsonNodeAnnouncement = objectMapper.valueToTree(announcement);
        jsonNodeAnnouncement.put("driver", driver.getLink("self").map(Link::getHref).get());

        announcementConsumer.create(jsonNodeAnnouncement);

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
    public String announcementDetails(@RequestParam(name="announcementURI") String URI, Model model){
        model.addAttribute("announcement", announcementConsumer.getAnnouncementByURI(URI));
        return "announcementDetails";
    }
}
