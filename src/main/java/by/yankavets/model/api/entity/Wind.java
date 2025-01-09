package by.yankavets.model.api.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Wind {

    private Double speed;
    @JsonProperty("deg")
    private Double windDirection;
    private Double gust;
}
