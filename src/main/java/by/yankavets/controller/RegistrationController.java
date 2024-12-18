package by.yankavets.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RegistrationController {


    @GetMapping("/sign_up")
    public String register() {
        return "sign_up";
    }
}
