package by.yankavets.controller;

import by.yankavets.dto.user.UserReadDto;
import by.yankavets.dto.weather.WeatherByLocationReadDto;
import by.yankavets.service.LocationService;
import by.yankavets.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.UUID;

import static by.yankavets.constant.ParametersAndAttribute.*;
import static by.yankavets.constant.UrlPath.INDEX_PAGE;
import static by.yankavets.constant.UrlPath.INDEX_URL;

@Controller
public class HomeController {

    private final SessionService sessionService;
    private final LocationService locationService;

    @Autowired
    public HomeController(SessionService sessionService, LocationService locationService) {
        this.sessionService = sessionService;
        this.locationService = locationService;
    }

    @GetMapping(INDEX_URL)
    public String indexPage(@CookieValue(SESSION_UUID) UUID sessionId, Model model) {

        UserReadDto user = sessionService.findUserBySessionId(sessionId);
        model.addAttribute(USER_NAME, user.getName());
        List<WeatherByLocationReadDto> userLocations = locationService.findByUser(user);

        model.addAttribute(WEATHER_BY_LOCATION_LIST, userLocations);
        return INDEX_PAGE;
    }
}
