package by.yankavets.mapper.weather;

import by.yankavets.dto.weather.WeatherByLocationReadDto;
import by.yankavets.mapper.Mapper;
import by.yankavets.model.api.WeatherByLocationResponse;
import by.yankavets.util.TimeConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class WeatherByLocationReadMapper implements Mapper<WeatherByLocationResponse, WeatherByLocationReadDto> {

    @Value("${api.weather.icon}")
    private String weatherIconUrl;

    @Override
    public WeatherByLocationReadDto mapToDto(WeatherByLocationResponse entity) {
        return new WeatherByLocationReadDto(
                entity.getCity(),
                entity.getSunInfo().getCountryCode(),
                entity.getCoordinate().getLongitude(),
                entity.getCoordinate().getLatitude(),
                entity.getWeatherDetails().getTemperature(),
                entity.getWeatherDetails().getFeelsLike(),
                entity.getWeatherDetails().getHumidity(),
                generateWeatherIcon(entity.getWeather().get(0).getIcon()),
                entity.getWeather().get(0).getEnglishDescription(),
                TimeConverter.convertFromUTC(entity.getSunInfo().getSunrise()),
                TimeConverter.convertFromUTC(entity.getSunInfo().getSunset())
        );
    }

    private String generateWeatherIcon(String iconCode) {
        return String.format(weatherIconUrl, iconCode);
    }
}
