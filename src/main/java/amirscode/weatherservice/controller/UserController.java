package amirscode.weatherservice.controller;


import amirscode.weatherservice.entity.Weather;
import amirscode.weatherservice.service.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;


@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/cities")
    public ResponseEntity<?> citiesList() {
        return userService.citiesList();
    }

    @PostMapping("/subscribe")
    public ResponseEntity<?> subscribeToCity(@NonNull @RequestParam String cityName) {
        return userService.subscribeToCity(cityName);
    }

    @GetMapping("/subscriptions")
    public Flux<?> getSubscriptions() {
        return userService.getSubscriptions();
    }
}
