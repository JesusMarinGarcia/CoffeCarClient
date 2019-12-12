package es.uma.ingweb.coffeecar.controller;

import es.uma.ingweb.coffeecar.consumers.UserConsumer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {
    private final UserConsumer userConsumer;

    public UserController(UserConsumer userConsumer) {
        this.userConsumer = userConsumer;
    }

    @GetMapping("/profile")
    public String profile(){
        return "profile";
    }
}
