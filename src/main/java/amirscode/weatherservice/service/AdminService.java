package amirscode.weatherservice.service;

import amirscode.weatherservice.entity.City;
import amirscode.weatherservice.entity.User;
import amirscode.weatherservice.entity.Weather;
import amirscode.weatherservice.mapper.UserMapper;
import amirscode.weatherservice.model.CityRequest;
import amirscode.weatherservice.model.EditUserRequest;
import amirscode.weatherservice.model.WeatherDTO;
import amirscode.weatherservice.repository.CityRepository;
import amirscode.weatherservice.repository.UserRepository;
import amirscode.weatherservice.repository.WeatherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final CityRepository cityRepository;
    private final WeatherRepository weatherRepository;

    public ResponseEntity<?> userList() {

        return ResponseEntity.ok(userRepository
                .findAll()
                .stream()
                .map(userMapper)
                .collect(Collectors.toList()));
    }

    public ResponseEntity<?> userDetails(Long id) {

        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return ResponseEntity.ok(user.getSubscribedCities());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    public ResponseEntity<?> editUser(Long userId, EditUserRequest request) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setEnabled(request.isEnabled());
            user.setUsername(request.getUsername() != null ? request.getUsername() : user.getUsername());
            userRepository.save(user);

            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    public ResponseEntity<?> cityList() {
        List<City> cityList = cityRepository.findAll();
        return ResponseEntity.ok(cityList);
    }

    public ResponseEntity<?> editCity(Long cityId, CityRequest request) {

        Optional<City> cityOptional = cityRepository.findById(cityId);
        if (cityOptional.isPresent()) {
            City city = cityOptional.get();
            city.setAvailable(request.isAvailable());
            city.setName(request.getName() != null ? request.getName() : city.getName());
            cityRepository.save(city);

            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    public ResponseEntity<?> updateCityWeather(WeatherDTO weatherDTO, Long cityId) {

        try {
            Optional<City> cityOptional = cityRepository.findById(cityId);
            if (cityOptional.isPresent()) {
                City city = cityOptional.get();
                Weather weather = city.getWeather();

                weather.setTemp(weatherDTO.getTemp());
                weather.setHumidity(weatherDTO.getHumidity());
                weather.setPressure(weatherDTO.getPressure());
                weather.setWindSpeed(weatherDTO.getWindSpeed());
                weatherRepository.save(weather);
                cityRepository.save(city);

                return ResponseEntity.ok().body(city);
            }
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body(ex.getStackTrace());
        }
        return ResponseEntity.badRequest().build();
    }
}
