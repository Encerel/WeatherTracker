package by.yankavets.dto.location;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class LocationCreateDeleteDto {

    private String name;
    private Integer userId;
    private BigDecimal latitude;
    private BigDecimal longitude;

}
