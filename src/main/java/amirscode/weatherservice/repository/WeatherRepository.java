package amirscode.weatherservice.repository;

import amirscode.weatherservice.entity.Weather;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeatherRepository extends JpaRepository<Weather, Long> {
}
