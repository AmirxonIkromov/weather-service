package amirscode.weatherservice.mapper;

import amirscode.weatherservice.entity.City;
import amirscode.weatherservice.model.WeatherResponse;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class WeatherMapper implements Function<City, WeatherResponse> {
    @Override
    public WeatherResponse apply(City city) {
        return new WeatherResponse(
                city.getId(),
                city.getName(),
                city.getWeather().getId(),
                city.getWeather().getTemp(),
                city.getWeather().getWindSpeed(),
                city.getWeather().getPressure(),
                city.getWeather().getHumidity()
        );
    }
}
