package amirscode.weatherservice.mapper;

import amirscode.weatherservice.entity.City;
import amirscode.weatherservice.model.CityDTO;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class CityMapper implements Function<City, CityDTO> {
    @Override
    public CityDTO apply(City city) {
        return new CityDTO(
                city.getId(),
                city.getName()
        );
    }
}
