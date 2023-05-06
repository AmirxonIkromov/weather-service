package amirscode.weatherservice.service;

import amirscode.weatherservice.entity.City;
import amirscode.weatherservice.entity.User;
import amirscode.weatherservice.entity.Weather;
import amirscode.weatherservice.mapper.UserMapper;
import amirscode.weatherservice.model.*;
import amirscode.weatherservice.repository.CityRepository;
import amirscode.weatherservice.repository.UserRepository;
import amirscode.weatherservice.repository.WeatherRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdminServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private CityRepository cityRepository;

    @Mock
    private WeatherRepository weatherRepository;

    @InjectMocks
    private AdminService adminService;

    private User user;
    private City city;
    private Weather weather;
    private UserDTO userDTO;
    private CityDTO cityDTO;
    private WeatherDTO weatherDTO;

    @BeforeEach
    public void setup() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEnabled(true);

        userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setUsername("testuser");
        userDTO.setEnabled(true);

        Set<City> cities = new HashSet<>();
        city = new City();
        city.setId(1L);
        city.setName("New York");
        cities.add(city);
        user.setSubscribedCities(cities);

        cityDTO = new CityDTO();
        cityDTO.setId(1L);
        cityDTO.setName("New York");

        weather = new Weather();
        weather.setId(1L);
        weather.setTemp(70.0F);
        weather.setHumidity(60);
        weather.setPressure(1013);
        weather.setWindSpeed(5.0F);
        city.setWeather(weather);

        weatherDTO = new WeatherDTO();
        weatherDTO.setTemp(70.0F);
        weatherDTO.setHumidity(60);
        weatherDTO.setPressure(1013);
        weatherDTO.setWindSpeed(5.0F);
    }

    @Test
    public void testUserList() {
        List<User> userList = new ArrayList<>();
        userList.add(user);
        when(userRepository.findAll()).thenReturn(userList);
        when(userMapper.apply(user)).thenReturn(userDTO);

        ResponseEntity<?> response = adminService.userList();

        verify(userRepository).findAll();
        verify(userMapper).apply(user);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof List);
        assertEquals(userList.size(), ((List<?>) response.getBody()).size());
        assertEquals(userDTO, ((List<UserDTO>) response.getBody()).get(0));
    }

    @Test
    public void testUserDetails() {
        Long id = 1L;
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        ResponseEntity<?> response = adminService.userDetails(id);

        verify(userRepository).findById(id);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof Set);
        assertEquals(user.getSubscribedCities().size(), ((Set<?>) response.getBody()).size());
        assertEquals(city, ((Set<City>) response.getBody()).iterator().next());
    }

    @Test
    public void testUserDetailsWithInvalidId() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResponseEntity<?> responseEntity = adminService.userDetails(1L);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testEditUser() {

        Long userId = 1L;
        EditUserRequest request = new EditUserRequest();
        request.setEnabled(true);
        request.setUsername("new_username");

        User user = new User();
        user.setEnabled(false);
        user.setUsername("old_username");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        ResponseEntity<?> responseEntity = adminService.editUser(userId, request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(user.isEnabled());
        assertEquals("new_username", user.getUsername());
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(user);
    }


    @Test
    void testEditUserWithInvalidId() {

        EditUserRequest request = new EditUserRequest();
        request.setEnabled(true);
        request.setUsername("newUsername");
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<?> responseEntity = adminService.editUser(1L, request);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        verify(userRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void testCityList() {

        City city = new City();
        city.setId(1L);
        city.setName("City1");
        when(cityRepository.findAll()).thenReturn(Collections.singletonList(city));

        ResponseEntity<?> responseEntity = adminService.cityList();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Collections.singletonList(city), responseEntity.getBody());
        verify(cityRepository, times(1)).findAll();
    }

    @Test
    void testEditCityWithValidId() {

        City city = new City();
        city.setId(1L);
        city.setAvailable(true);
        CityRequest request = new CityRequest();
        request.setAvailable(false);
        request.setName("newName");
        when(cityRepository.findById(1L)).thenReturn(Optional.of(city));

        ResponseEntity<?> responseEntity = adminService.editCity(1L, request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(request.isAvailable(), city.isAvailable());
        assertEquals(request.getName(), city.getName());
        verify(cityRepository, times(1)).findById(1L);
        verify(cityRepository, times(1)).save(city);
    }

    @Test
    void testEditCityWithInvalidId() {

        CityRequest request = new CityRequest();
        request.setAvailable(false);
        request.setName("newName");
        when(cityRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<?> responseEntity = adminService.editCity(1L, request);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        verify(cityRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(cityRepository);
    }

    @Test
    void testUpdateCityWeatherWithValidCityId() {

        City city = new City();
        city.setId(1L);
        Weather weather = new Weather();
        city.setWeather(weather);
        WeatherDTO weatherDTO = new WeatherDTO();
        weatherDTO.setTemp(20.0F);
        weatherDTO.setHumidity(50);
        weatherDTO.setPressure(1013);
        weatherDTO.setWindSpeed(10F);
        when(cityRepository.findById(1L)).thenReturn(Optional.of(city));

        ResponseEntity<?> responseEntity = adminService.updateCityWeather(weatherDTO, 1L);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(city, responseEntity.getBody());
        assertEquals(weatherDTO.getTemp(), weather.getTemp());
        assertEquals(weatherDTO.getHumidity(), weather.getHumidity());
        assertEquals(weatherDTO.getPressure(), weather.getPressure());
        assertEquals(weatherDTO.getWindSpeed(), weather.getWindSpeed());
        verify(cityRepository, times(1)).findById(1L);
        verify(weatherRepository, times(1)).save(weather);
        verify(cityRepository, times(1)).save(city);
    }

    @Test
    void testUpdateCityWeatherWithInvalidCityId() {

        WeatherDTO weatherDTO = new WeatherDTO();
        when(cityRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<?> responseEntity = adminService.updateCityWeather(weatherDTO, 1L);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        verify(cityRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(cityRepository);
        verifyNoMoreInteractions(weatherRepository);
    }
}
