package by.yankavets.model.api.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class WeatherDetails {

    @JsonProperty("temp")
    private Double temperature;

    @JsonProperty("feels_like")
    private Double feelsLike;

    @JsonProperty("temp_min")
    private Double minimalTemperature;

    @JsonProperty("temp_max")
    private Double maximalTemperature;

    private Integer pressure;

    private Integer humidity;

    @JsonProperty("sea_level")
    private Integer seaLevel;

    @JsonProperty("grnd_level")
    private Integer groundLevel;
}
