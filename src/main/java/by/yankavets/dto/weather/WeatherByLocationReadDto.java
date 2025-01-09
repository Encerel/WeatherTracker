package by.yankavets.dto.weather;


import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalTime;

@Value
public class WeatherByLocationReadDto {

    String city;
    String country;
    BigDecimal longitude;
    BigDecimal latitude;
    Double temperature;
    Double feelsLike;
    Integer humidity;
    String image;
    String description;
    LocalTime sunrise;
    LocalTime sunset;

}
