package es.uma.ingweb.coffeecar.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ClientController {

    @RequestMapping("/index")
    public String index(){

        return "index";
    }

    @RequestMapping(value = "/createAnnoucement")
    public String createAnnoucement(){
        return "createAnnoucement";
    }

    @RequestMapping(value = "/annoucement")
    public String annoucementDetails(){
        return "annoucement";
    }
}
