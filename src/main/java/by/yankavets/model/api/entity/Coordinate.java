package by.yankavets.model.api.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Coordinate {

    @JsonProperty("lon")
    private BigDecimal longitude;
    @JsonProperty("lat")
    private BigDecimal latitude;

}
