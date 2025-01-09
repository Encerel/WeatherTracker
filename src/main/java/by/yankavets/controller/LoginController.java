package by.yankavets.controller;

import by.yankavets.dto.user.UserCreateDto;
import by.yankavets.dto.user.UserLoginDto;
import by.yankavets.dto.user.UserReadDto;
import by.yankavets.model.entity.SessionEntity;
import by.yankavets.service.SessionService;
import by.yankavets.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static by.yankavets.constant.ParametersAndAttribute.*;
import static by.yankavets.constant.UrlPath.*;

@Controller
public class LoginController {

    private final UserService userService;
    private final SessionService sessionService;

    @Autowired
    public LoginController(UserService userService, SessionService sessionService) {
        this.userService = userService;
        this.sessionService = sessionService;
    }


    @GetMapping(SIGN_IN_URL)
    public String loginPage(@RequestParam(value = SUCCESS, required = false) String successParam,
                            @ModelAttribute(USER) UserCreateDto userCreateDto,
                            Model model) {
        if (successParam != null) {
            model.addAttribute(SUCCESS, true);
        }
        return SIGN_IN_PAGE;
    }


    @PostMapping(SIGN_IN_URL)
    public String login(@ModelAttribute(USER) @Valid UserLoginDto user,
                        BindingResult bindingResult,
                        Model model,
                        HttpServletResponse response) {

        if (bindingResult.hasErrors()) {
            return SIGN_IN_URL;
        }

        try {
            UserReadDto foundUser = userService.findByEmailAndPassword(user);
            SessionEntity createdSession = sessionService.createByUserId(foundUser.getId());
            Cookie cookie = new Cookie(SESSION_UUID, createdSession.getId().toString());
            cookie.setMaxAge(60 * 60 * 24);
            response.addCookie(cookie);
            return REDIRECT + INDEX_URL;
        } catch (RuntimeException e) {
            model.addAttribute(ERROR, e.getMessage());
            return SIGN_IN_URL;
        }

    }

}
