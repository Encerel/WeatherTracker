package by.yankavets.service;

import by.yankavets.dao.impl.LocationDao;
import by.yankavets.dto.location.LocationCreateDeleteDto;
import by.yankavets.dto.user.UserReadDto;
import by.yankavets.dto.weather.WeatherByLocationReadDto;
import by.yankavets.exception.user.UserWithSuchIdNotFoundException;
import by.yankavets.mapper.location.LocationCreateMapper;
import by.yankavets.mapper.weather.WeatherByLocationReadMapper;
import by.yankavets.model.api.LocationByNameResponse;
import by.yankavets.model.api.WeatherByLocationResponse;
import by.yankavets.model.api.entity.Coordinate;
import by.yankavets.model.entity.Location;
import by.yankavets.model.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LocationServiceTest {

    @Mock
    private LocationDao locationDao;
    @Mock
    private UserService userService;
    @Mock
    private WeatherByLocationReadMapper weatherByLocationReadMapper;
    @Mock
    private LocationCreateMapper locationCreateMapper;
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private ObjectMapper objectMapper;
    @InjectMocks
    private LocationService locationService;


    private User mockUser;
    private UserReadDto userReadDto;
    private static final Integer USER_ID = 1;
    private static final String USER_NAME = "User dummy name";
    private static final String USER_EMAIL = "test@gmail.com";
    private static final String ENCRYPTED_PASSWORD = "encryptedPassword";
    private Location mockLocation;
    private static final Integer LOCATION_ID = 1;
    private static final String LOCATION_NAME = "Location dummy name";
    private static final BigDecimal LATITUDE = BigDecimal.valueOf(30.5);
    private static final BigDecimal LONGITUDE = BigDecimal.valueOf(30.5);
    private List<Location> mockLocations;
    private WeatherByLocationReadDto weatherByLocationReadDto;
    public static final String CITY = "Moscow";
    public static final String COUNTRY = "RU";
    public static final Double TEMPERATURE = 15d;
    public static final Double FEELS_LIKE = 13d;
    public static final Integer HUMIDITY = 50;
    public static final String IMAGE = "image";
    public static final String DESCRIPTION = "description";
    public static final LocalTime SUNSET = LocalTime.of(22, 0);
    public static final LocalTime SUNRISE = LocalTime.of(8, 0);
    private LocationByNameResponse locationByNameResponse;
    private static final String STATE = "state";
    private static final String JSON = "json";
    private List<LocationByNameResponse> locationsByNameResponse;
    private List<WeatherByLocationReadDto> weatherByLocationList;
    private LocationCreateDeleteDto locationCreateDeleteDto;


    @BeforeEach
    void setUp() {
        mockUser = User.builder()
                .id(USER_ID)
                .name(USER_NAME)
                .email(USER_EMAIL)
                .password(ENCRYPTED_PASSWORD)
                .build();

        userReadDto = UserReadDto.builder()
                .id(USER_ID)
                .name(USER_NAME)
                .email(USER_EMAIL)
                .build();
        mockLocation = Location.builder()
                .id(LOCATION_ID)
                .name(LOCATION_NAME)
                .latitude(LATITUDE)
                .longitude(LONGITUDE)
                .user(mockUser)
                .build();
        mockLocations = List.of(mockLocation, mockLocation);
        weatherByLocationReadDto = new WeatherByLocationReadDto(
                CITY,
                COUNTRY,
                LONGITUDE,
                LATITUDE,
                TEMPERATURE,
                FEELS_LIKE,
                HUMIDITY,
                IMAGE,
                DESCRIPTION,
                SUNSET,
                SUNRISE
        );


        locationByNameResponse = LocationByNameResponse.builder()
                .locationName(LOCATION_NAME)
                .latitude(LATITUDE)
                .longitude(LONGITUDE)
                .countryCode(COUNTRY)
                .state(STATE)
                .build();

        locationsByNameResponse = List.of(locationByNameResponse);

        weatherByLocationList = List.of(weatherByLocationReadDto, weatherByLocationReadDto);

        locationCreateDeleteDto = new LocationCreateDeleteDto(
                LOCATION_NAME,
                USER_ID,
                LATITUDE,
                LONGITUDE
        );


    }

    @Test
    void findByUserWhenUserFoundAndThereIsAtLeastOneLocation() {
        ResponseEntity<WeatherByLocationResponse> response = new ResponseEntity<>(
                new WeatherByLocationResponse(),
                HttpStatusCode.valueOf(200));
        doReturn(Optional.of(mockUser)).when(userService).findById(USER_ID);
        doReturn(mockLocations).when(locationDao).findByUser(mockUser);
        doReturn(response).when(restTemplate).getForEntity(anyString(), eq(WeatherByLocationResponse.class));
        doReturn(weatherByLocationReadDto).when(weatherByLocationReadMapper).mapToDto(any(WeatherByLocationResponse.class));
        List<WeatherByLocationReadDto> actualResult = locationService.findByUser(userReadDto);
        assertEquals(weatherByLocationList, actualResult);
    }

    @Test
    void findByUserWhenUserNotFound() {
        doReturn(Optional.empty()).when(userService).findById(USER_ID);

        Exception ex = assertThrows(
                UserWithSuchIdNotFoundException.class,
                () -> locationService.findByUser(userReadDto)
        );

        assertEquals("User with id %d not found!".formatted(USER_ID), ex.getMessage());
    }


    @Test
    void findByUserWhenUserFoundAndThereAreAtNoLocations() {
        doReturn(Optional.of(mockUser)).when(userService).findById(USER_ID);
        doReturn(Collections.emptyList()).when(locationDao).findByUser(mockUser);

        List<WeatherByLocationReadDto> foundLocations = locationService.findByUser(userReadDto);

        assertTrue(foundLocations.isEmpty());

    }

    @Test
    void findWeatherByValidCoordinate() {
        ResponseEntity<WeatherByLocationResponse> response = new ResponseEntity<>(
                new WeatherByLocationResponse(),
                HttpStatusCode.valueOf(200));
        doReturn(response).when(restTemplate).getForEntity(anyString(), eq(WeatherByLocationResponse.class));
        doReturn(weatherByLocationReadDto).when(weatherByLocationReadMapper).mapToDto(any(WeatherByLocationResponse.class));

        WeatherByLocationReadDto actualResult = locationService.findWeatherByCoordinate(new Coordinate(LONGITUDE, LATITUDE));
        assertEquals(weatherByLocationReadDto, actualResult);
    }

    @Test
    @SneakyThrows
    void findLocationsByNameWhenLocationsFound() {
        TypeFactory typeFactory = new ObjectMapper().getTypeFactory();
        doReturn(JSON).when(restTemplate).getForObject(anyString(), eq(String.class));
        doReturn(typeFactory).when(objectMapper).getTypeFactory();
        doReturn(locationsByNameResponse).when(objectMapper).readValue(anyString(), any(CollectionType.class));

        List<LocationByNameResponse> actualResult = locationService.findLocationsByName(LOCATION_NAME);

        assertEquals(locationsByNameResponse, actualResult);
    }

    @Test
    @SneakyThrows
    void findLocationsByNameWhenLocationsNotFound() {
        TypeFactory typeFactory = new ObjectMapper().getTypeFactory();
        doReturn(JSON).when(restTemplate).getForObject(anyString(), eq(String.class));
        doReturn(typeFactory).when(objectMapper).getTypeFactory();
        doReturn(Collections.emptyList()).when(objectMapper).readValue(anyString(), any(CollectionType.class));

        List<LocationByNameResponse> actualResult = locationService.findLocationsByName(LOCATION_NAME);

        assertEquals(Collections.emptyList(), actualResult);
    }

    @Test
    void save() {
        doReturn(mockLocation).when(locationDao).save(mockLocation);
        doReturn(mockLocation).when(locationCreateMapper).mapToEntity(locationCreateDeleteDto);

        Location actualResult = locationService.save(locationCreateDeleteDto);
        assertEquals(mockLocation.getId(), actualResult.getId());
    }

    @Test
    void findByCoordinateAndUserWhenUserAndCoordinateValid() {
        Coordinate coordinate = new Coordinate(LONGITUDE, LATITUDE);
        doReturn(Optional.of(mockLocation)).when(locationDao).findByCoordinateAndUser(coordinate, mockUser);

        Optional<Location> actualResult = locationService.findByCoordinateAndUser(coordinate, mockUser);

        assertTrue(actualResult.isPresent());
        assertEquals(mockLocation, actualResult.get());

    }

    @Test
    void findByCoordinateAndUserWhenUserOrCoordinateInvalid() {
        Coordinate coordinate = new Coordinate(LONGITUDE, LATITUDE);
        doReturn(Optional.empty()).when(locationDao).findByCoordinateAndUser(coordinate, mockUser);

        Optional<Location> actualResult = locationService.findByCoordinateAndUser(coordinate, mockUser);

        assertTrue(actualResult.isEmpty());
    }

    @Test
    void deleteWithSuccess() {
        boolean expectedResult = true;
        doReturn(expectedResult).when(locationDao).delete(USER_ID);
        boolean actualResult = locationService.delete(mockLocation);
        assertEquals(expectedResult, actualResult);
        verify(locationDao, times(1)).delete(USER_ID);
    }

    @Test
    void deleteWithoutSuccess() {
        boolean expectedResult = false;
        doReturn(expectedResult).when(locationDao).delete(USER_ID);
        boolean actualResult = locationService.delete(mockLocation);
        assertEquals(expectedResult, actualResult);
        verify(locationDao, times(1)).delete(USER_ID);
    }
}