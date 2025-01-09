package by.yankavets.model.api.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SunInfo {

    @JsonProperty("country")
    private String countryCode;
    private Long sunrise;
    private Long sunset;
}
