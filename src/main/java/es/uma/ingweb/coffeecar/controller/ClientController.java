package es.uma.ingweb.coffeecar.controller;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ClientController {

    @RequestMapping("/index")                       //Inicio
    public String index(){return "index";}

    @RequestMapping(value = "/announce")            //Crear anuncio
    public String announce(){
        return "announce";
    }

    @RequestMapping(value = "/annoucement")         //Detalles de un anuncio
    public String annoucement(OAuth2AuthenticationToken auth2AuthenticationToken){
        System.out.println(auth2AuthenticationToken.getPrincipal().getAttributes());
        return "annoucement";
    }
}
