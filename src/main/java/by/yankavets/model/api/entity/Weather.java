package by.yankavets.model.api.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Weather {

    private Integer id;
    @JsonProperty("main")
    private String englishDescription;
    private String description;
    private String icon;
}
