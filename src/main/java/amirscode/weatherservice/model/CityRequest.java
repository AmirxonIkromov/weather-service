package amirscode.weatherservice.model;

import lombok.Data;

@Data
public class CityRequest {

    private String name;
    private boolean available;
}
