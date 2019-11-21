package es.uma.ingweb.coffeecar.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ClientController {

    @RequestMapping("/inicio")
    public String hola(){

        return "inicio";
    }

    @RequestMapping(value = "/crearAnuncio")
    public String anuncios(){
        return "crearAnuncio";
    }
}
