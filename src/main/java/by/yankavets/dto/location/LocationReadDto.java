package by.yankavets.dto.location;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LocationReadDto {

    private String name;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String countryCode;
    private String state;

}
