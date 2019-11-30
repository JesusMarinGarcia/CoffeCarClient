package es.uma.ingweb.coffeecar.controller;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ClientController {

    @RequestMapping("/inicio")
    public String hola(){

        return "inicio";
    }

    @RequestMapping(value = "/crearAnuncio")
    public String anuncios(OAuth2AuthenticationToken auth2AuthenticationToken){
        System.out.println(auth2AuthenticationToken.getPrincipal().getAttributes());
        return "crearAnuncio";
    }
}
