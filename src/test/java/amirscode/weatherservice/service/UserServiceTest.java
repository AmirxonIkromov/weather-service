package amirscode.weatherservice.service;

import amirscode.weatherservice.entity.City;
import amirscode.weatherservice.entity.User;
import amirscode.weatherservice.entity.Weather;
import amirscode.weatherservice.mapper.CityMapper;
import amirscode.weatherservice.mapper.WeatherMapper;
import amirscode.weatherservice.model.CityDTO;
import amirscode.weatherservice.model.WeatherResponse;
import amirscode.weatherservice.repository.CityRepository;
import amirscode.weatherservice.repository.UserRepository;
import amirscode.weatherservice.repository.WeatherRepository;
import liquibase.pro.packaged.W;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CityRepository cityRepository;

    @Mock
    private WeatherMapper weatherMapper;

    @Mock
    private CityMapper cityMapper;

    @InjectMocks
    private UserService userService;

    private City city1;
    private City city2;
    private City city3;
    private User user;

    @BeforeEach
    public void setUp() {
        Weather weather = new Weather(1L, 33F, 12F, 15, 59);
        Weather weather2 = new Weather(2L, 44F, 13F, 17, 62);
        Weather weather3 = new Weather(3L, 55F, 16F, 23, 43);
        city1 = new City(1L, "London");
        city2 = new City(2L, "Paris");
        city3 = new City(3L, "Tokyo");

        city1.setAvailable(true);
        city1.setWeather(weather);
        city2.setAvailable(true);
        city2.setWeather(weather2);
        city3.setAvailable(false);
        city2.setWeather(weather3);

        user = new User(1L, "user1", true);
        user.setSubscribedCities(new HashSet<>(Set.of(city1, city2)));
    }

    @Test
    public void testCitiesList() {
        when(cityRepository.findAllByAvailableIs(true)).thenReturn(List.of(city1, city2));
        when(cityMapper.apply(city1)).thenReturn(new CityDTO());
        when(cityMapper.apply(city2)).thenReturn(new CityDTO());

        ResponseEntity<?> responseEntity = userService.citiesList();

        verify(cityRepository).findAllByAvailableIs(true);
        verify(cityMapper, times(2)).apply(any(City.class));
        verifyNoMoreInteractions(cityRepository, cityMapper);

        assert responseEntity.getStatusCodeValue() == 200;
        assert responseEntity.getBody() instanceof Set;
        assert ((Set<?>) responseEntity.getBody()).size() != 2;
    }

    @Test
    public void testSubscribeToCity() {
        when(cityRepository.findByName("Paris")).thenReturn(Optional.of(city2));
        when(userRepository.save(any(User.class))).thenReturn(user);
        SecurityContextHolder.setContext(mock(SecurityContext.class));
        Authentication authentication = mock(Authentication.class);
        when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);

        ResponseEntity<?> responseEntity = userService.subscribeToCity("Paris");

        verify(cityRepository).findByName("Paris");
        verify(userRepository).save(user);
        verifyNoMoreInteractions(cityRepository, userRepository);

        assert responseEntity.getStatusCodeValue() == 202;
    }

    @Test
    public void testGetSubscriptions() {
        SecurityContextHolder.setContext(mock(SecurityContext.class));
        Authentication authentication = mock(Authentication.class);
        when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);

        Flux<?> flux = userService.getSubscriptions();

        StepVerifier.create(flux)
                .expectNextCount(1)
                .verifyComplete();
    }

}