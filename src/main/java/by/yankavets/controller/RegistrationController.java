package by.yankavets.controller;

import by.yankavets.constant.ParametersAndAttribute;
import by.yankavets.dto.UserCreateDto;
import by.yankavets.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import static by.yankavets.constant.ParametersAndAttribute.ERROR;
import static by.yankavets.constant.ParametersAndAttribute.USER;

@Controller
public class RegistrationController {

    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/sign_up")
    public String registrationPage(@ModelAttribute(USER) UserCreateDto user) {
        return "sign_up";
    }

    @PostMapping("/sign_up")
    public String register(@ModelAttribute(USER) @Valid UserCreateDto user, BindingResult result, Model model) {

        if (result.hasErrors()) {
            return "sign_up";
        }

        try {
            userService.save(user);
        } catch (RuntimeException e) {
            model.addAttribute(ERROR, e.getMessage());
            return "sign_up";
        }
        return "redirect:sign_in?success";
    }
}
