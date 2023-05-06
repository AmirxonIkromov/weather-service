package amirscode.weatherservice.repository;

import amirscode.weatherservice.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CityRepository extends JpaRepository<City, Long> {


    List<City> findAllByAvailableIs(boolean available);

    Optional<City> findByName(String cityName);


}
