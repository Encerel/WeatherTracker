package by.yankavets.controller;

import by.yankavets.dto.location.LocationCreateDeleteDto;
import by.yankavets.dto.location.LocationReadDto;
import by.yankavets.dto.user.UserReadDto;
import by.yankavets.mapper.location.LocationReadMapper;
import by.yankavets.model.api.entity.Coordinate;
import by.yankavets.model.entity.Location;
import by.yankavets.model.entity.SessionEntity;
import by.yankavets.model.entity.User;
import by.yankavets.service.LocationService;
import by.yankavets.service.SessionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static by.yankavets.constant.ParametersAndAttribute.*;
import static by.yankavets.constant.UrlPath.*;

@Controller
@RequestMapping(LOCATIONS_URL)
public class LocationController {

    private final LocationService locationService;
    private final SessionService sessionService;
    private final LocationReadMapper locationReadMapper;


    @Autowired
    public LocationController(LocationService locationService, SessionService sessionService, LocationReadMapper locationReadMapper) {
        this.locationService = locationService;
        this.sessionService = sessionService;
        this.locationReadMapper = locationReadMapper;
    }

    @GetMapping(SEARCH_URL)
    public String findLocationsByName(@CookieValue(SESSION_UUID) UUID sessionId,
                                      @RequestParam(value = LOCATION_NAME, required = false) String name,
                                      Model model) {

        if (name == null || name.isBlank()) {
            return REDIRECT + INDEX_URL;
        }
        UserReadDto user = sessionService.findUserBySessionId(sessionId);
        model.addAttribute(USER_NAME, user.getName());

        try {
            List<LocationReadDto> locationsByName = locationReadMapper.fromApiEntityListToDtoList(locationService.findLocationsByName(name));
            model.addAttribute(LOCATION_LIST, locationsByName);
            return SEARCH_PAGE;
        } catch (JsonProcessingException e) {
            return ERROR_PAGE;
        }
    }

    @PostMapping(SEARCH_URL)
    public String saveLocationForUser(@CookieValue(SESSION_UUID) UUID sessionId,
                                      @ModelAttribute(LOCATION) LocationCreateDeleteDto location) {

        Optional<SessionEntity> session = sessionService.findById(sessionId);

        User user = session.get().getUser();
        location.setUserId(user.getId());
        Coordinate coordinate = new Coordinate(location.getLongitude(), location.getLatitude());
        Optional<Location> foundLocation = locationService.findByCoordinateAndUser(coordinate, user);

        if (foundLocation.isEmpty()) {
            locationService.save(location);
            return REDIRECT + INDEX_URL;
        }

        return REDIRECT + INDEX_URL;
    }

    @DeleteMapping(DELETE_URL)
    public String deleteFromUserByCoordinate(@CookieValue(SESSION_UUID) UUID sessionId,
                                             @RequestParam(LONGITUDE) BigDecimal longitude,
                                             @RequestParam(LATITUDE) BigDecimal latitude) {


        Optional<SessionEntity> session = sessionService.findById(sessionId);
        User user = session.get().getUser();
        Coordinate coordinate = new Coordinate(longitude, latitude);
        Optional<Location> foundLocation = locationService.findByCoordinateAndUser(coordinate, user);

        foundLocation.ifPresent(locationService::delete);

        return REDIRECT + INDEX_URL;
    }


}
