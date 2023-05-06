package amirscode.weatherservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeatherResponse {

    private Long cityId;
    private String name;

    private Long WeatherId;
    private Float temp;
    private Float windSpeed;
    private Integer pressure;
    private Integer humidity;
}
