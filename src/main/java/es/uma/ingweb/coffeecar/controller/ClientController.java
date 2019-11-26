package es.uma.ingweb.coffeecar.controller;

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
    public String annoucement(){
        return "annoucement";
    }
}
