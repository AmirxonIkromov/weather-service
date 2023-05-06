package amirscode.weatherservice.controller;

import amirscode.weatherservice.model.CityRequest;
import amirscode.weatherservice.model.EditUserRequest;
import amirscode.weatherservice.model.WeatherDTO;
import amirscode.weatherservice.service.AdminService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;


    @GetMapping("/user_list")
    public ResponseEntity<?> userList() {
        return adminService.userList();
    }

    @GetMapping("/user_details")
    public ResponseEntity<?> userDetails(@RequestParam Long id) {
        return adminService.userDetails(id);
    }

    @PostMapping("edit_user/{userId}")
    public ResponseEntity<?> editUser(@PathVariable Long userId, @RequestBody EditUserRequest request) {
        return adminService.editUser(userId, request);
    }

    @GetMapping("/city_list")
    public ResponseEntity<?> cityList() {
        return adminService.cityList();
    }

    @PostMapping("/edit_city")
    public ResponseEntity<?> editCity(@RequestParam Long cityId, @RequestBody CityRequest request) {
        return adminService.editCity(cityId, request);
    }

    @PostMapping("/update_city_weather/{cityId}")
    public ResponseEntity<?> updateCityWeather(@RequestBody WeatherDTO weatherDTO, @PathVariable Long cityId) {
        return adminService.updateCityWeather(weatherDTO, cityId);
    }

}
