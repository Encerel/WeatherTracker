package by.yankavets.controller;

import by.yankavets.dto.user.UserCreateDto;
import by.yankavets.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import static by.yankavets.constant.ParametersAndAttribute.*;
import static by.yankavets.constant.UrlPath.*;

@Controller
public class RegistrationController {

    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping(SIGN_UP_URL)
    public String registrationPage(@ModelAttribute(USER) UserCreateDto user) {
        return SIGN_UP_PAGE;
    }

    @PostMapping(SIGN_UP_URL)
    public String register(@ModelAttribute(USER) @Valid UserCreateDto user, BindingResult result, Model model) {

        if (result.hasErrors()) {
            return SIGN_UP_URL;
        }

        try {
            userService.save(user);
        } catch (RuntimeException e) {
            model.addAttribute(ERROR, e.getMessage());
            return SIGN_UP_URL;
        }
        return REDIRECT + SIGN_IN_URL + "?" + SUCCESS;
    }

}
