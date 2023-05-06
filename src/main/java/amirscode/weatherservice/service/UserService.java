package amirscode.weatherservice.service;

import amirscode.weatherservice.entity.City;
import amirscode.weatherservice.entity.User;
import amirscode.weatherservice.mapper.CityMapper;
import amirscode.weatherservice.mapper.WeatherMapper;
import amirscode.weatherservice.model.CityDTO;
import amirscode.weatherservice.repository.CityRepository;
import amirscode.weatherservice.repository.UserRepository;
import amirscode.weatherservice.repository.WeatherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CityRepository cityRepository;
    private final WeatherMapper weatherMapper;
    private final CityMapper cityMapper;

    public ResponseEntity<?> citiesList() {

        Set<CityDTO> allAvailableCities = cityRepository
                .findAllByAvailableIs(true)
                .stream()
//                .filter(City::isAvailable)
                .map(cityMapper)
                .collect(Collectors.toSet());

        return ResponseEntity.ok().body(allAvailableCities);
    }

    public ResponseEntity<?> subscribeToCity(String cityName) {
        try {
            Optional<City> cityOptional = cityRepository.findByName(cityName);
            if (cityOptional.isPresent()) {
                City city = cityOptional.get();
                User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                Set<City> subscribedCities = user.getSubscribedCities();
                subscribedCities.add(city);
                userRepository.save(user);

                return ResponseEntity.accepted().build();
            }
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body(ex.getStackTrace());
        }
        return ResponseEntity.badRequest().build();
    }

    public Flux<?> getSubscriptions() {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Set<City> subscribedCities = user.getSubscribedCities();

        return Flux
                .just(subscribedCities
                        .stream()
                        .filter(City::isAvailable)
                        .map(weatherMapper))
                .log()
                .doOnError(Throwable::printStackTrace);
    }

}
