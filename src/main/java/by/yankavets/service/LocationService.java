package by.yankavets.service;

import by.yankavets.dao.impl.LocationDao;
import by.yankavets.dto.location.LocationCreateDeleteDto;
import by.yankavets.dto.user.UserReadDto;
import by.yankavets.dto.weather.WeatherByLocationReadDto;
import by.yankavets.exception.location.LocationNotFound;
import by.yankavets.exception.user.UserWithSuchIdNotFoundException;
import by.yankavets.mapper.location.LocationCreateMapper;
import by.yankavets.mapper.weather.WeatherByLocationReadMapper;
import by.yankavets.model.api.LocationByNameResponse;
import by.yankavets.model.api.WeatherByLocationResponse;
import by.yankavets.model.api.entity.Coordinate;
import by.yankavets.model.entity.Location;
import by.yankavets.model.entity.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static by.yankavets.constant.ParametersAndAttribute.*;

@Service
@Transactional(readOnly = true)
public class LocationService {


    private final LocationDao locationDao;
    private final UserService userService;
    private final WeatherByLocationReadMapper weatherByLocationReadMapper;
    private final LocationCreateMapper locationCreateMapper;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${api.weather.url}")
    private String weatherByLocationApiUrl;

    @Value("${api.location.url}")
    private String locationByNameApiUrl;

    @Value("${api.key}")
    private String apiKey;
    public static final String UNITS_METRIC = "metric";
    public static final String LANGUAGE_RU = "ru";
    public static final String MAX_LIMIT = "5";


    @Autowired
    public LocationService(LocationDao locationDao, UserService userService, WeatherByLocationReadMapper weatherByLocationReadMapper, LocationCreateMapper locationCreateMapper, RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.locationDao = locationDao;
        this.userService = userService;
        this.weatherByLocationReadMapper = weatherByLocationReadMapper;
        this.locationCreateMapper = locationCreateMapper;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }


    public List<WeatherByLocationReadDto> findByUser(UserReadDto user) {

        Optional<User> foundUser = userService.findById(user.getId());

        if (foundUser.isEmpty()) {
            throw new UserWithSuchIdNotFoundException(user.getId());
        }

        List<Location> locations = locationDao.findByUser(foundUser.get());
        List<WeatherByLocationReadDto> locationDtos = new ArrayList<>();

        for (Location location : locations) {
            Coordinate coordinate = new Coordinate(location.getLongitude(), location.getLatitude());
            locationDtos.add(findWeatherByCoordinate(coordinate));
        }

        return locationDtos;
    }

    public WeatherByLocationReadDto findWeatherByCoordinate(Coordinate locationCoordinate) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(LAT, String.valueOf(locationCoordinate.getLatitude()));
        parameters.put(LON, String.valueOf(locationCoordinate.getLongitude()));
        parameters.put(UNITS, UNITS_METRIC);
        parameters.put(LANGUAGE, LANGUAGE_RU);
        parameters.put(APP_ID, apiKey);
        String url = generateUrl(weatherByLocationApiUrl, parameters);

        ResponseEntity<WeatherByLocationResponse> response = restTemplate.getForEntity(url, WeatherByLocationResponse.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            WeatherByLocationResponse weather = response.getBody();
            return weatherByLocationReadMapper.mapToDto(weather);
        }

        throw new LocationNotFound();
    }

    public List<LocationByNameResponse> findLocationsByName(String name) throws JsonProcessingException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(QUERY, name);
        parameters.put(APP_ID, apiKey);
        parameters.put(LIMIT, MAX_LIMIT);
        String url = generateUrl(locationByNameApiUrl, parameters);

        String json = restTemplate.getForObject(url, String.class);
        CollectionType collectionType = objectMapper.getTypeFactory()
                .constructCollectionType(List.class, LocationByNameResponse.class);

        return objectMapper.readValue(
                json,
                collectionType
        );
    }

    public Location save(LocationCreateDeleteDto location) {
        return locationDao.save(locationCreateMapper.mapToEntity(location));
    }

    public Optional<Location> findByCoordinateAndUser(Coordinate coordinate, User user) {
        return locationDao.findByCoordinateAndUser(coordinate, user);

    }

    public boolean delete(Location location) {
        return locationDao.delete(location.getId());
    }

    private String generateUrl(String apiUrl, Map<String, String> parameters) {
        StringBuilder url = new StringBuilder();
        url.append(apiUrl);
        url.append("?");
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            url.append(entry.getKey()).append("=").append(entry.getValue());
            url.append("&");
        }
        return url.substring(0, url.toString().length() - 1);
    }
}
