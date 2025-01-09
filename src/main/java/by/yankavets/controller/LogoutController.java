package by.yankavets.controller;

import by.yankavets.service.SessionService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.UUID;

import static by.yankavets.constant.ParametersAndAttribute.SESSION_UUID;
import static by.yankavets.constant.UrlPath.*;

@Controller
public class LogoutController {


    private final SessionService sessionService;

    @Autowired
    public LogoutController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping(LOGOUT_URL)
    public String logout(@CookieValue(SESSION_UUID) UUID sessionId, HttpServletResponse response) {
        sessionService.deleteById(sessionId);
        Cookie cookie = new Cookie(SESSION_UUID, sessionId.toString());
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return REDIRECT + SIGN_IN_URL;
    }
}
