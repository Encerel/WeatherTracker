package by.yankavets.model.api;

import by.yankavets.model.api.entity.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class WeatherByLocationResponse {

    @JsonProperty("coord")
    private Coordinate coordinate;

    private List<Weather> weather;

    private String base;

    @JsonProperty("main")
    private WeatherDetails weatherDetails;

    private Integer visibility;

    private Wind wind;

    private Clouds clouds;

    @JsonProperty("dt")
    private Long date;

    @JsonProperty("sys")
    private SunInfo sunInfo;

    private Integer timezone;
    private Integer id;

    @JsonProperty("name")
    private String city;

    @JsonProperty("cod")
    private Integer responseCode;

}
