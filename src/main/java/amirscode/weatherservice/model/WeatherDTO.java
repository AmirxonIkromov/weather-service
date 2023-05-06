package amirscode.weatherservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeatherDTO {

    private Float temp;
    private Float windSpeed;
    private Integer pressure;
    private Integer humidity;
}
