package amirscode.weatherservice.service;


import amirscode.weatherservice.config.JwtService;
import amirscode.weatherservice.entity.User;
import amirscode.weatherservice.enums.Role;
import amirscode.weatherservice.model.LoginDTO;
import amirscode.weatherservice.model.RegisterDTO;
import amirscode.weatherservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;


    public ResponseEntity<?> register(RegisterDTO registerDTO) {

        User user = new User();

        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setUsername(registerDTO.getUsername());
        user.setRole(Role.USER);
        userRepository.save(user);

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        registerDTO.getUsername(),
                        registerDTO.getPassword()
                )
        );
        var jwtToken = jwtService.generateToken(user);
        return ResponseEntity.ok(jwtToken);
    }

    public ResponseEntity<?> login(LoginDTO loginDTO) {

        Optional<User> optionalUser = userRepository.findByUsername(loginDTO.getUsername());
        if(optionalUser.isPresent()) {
            User user = optionalUser.get();
            try {
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginDTO.getUsername(),
                                loginDTO.getPassword()
                        )
                );
            }catch (Exception ex){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Wrong username or password");
            }
            String jwtToken = jwtService.generateToken(user);
            return ResponseEntity.ok(jwtToken);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No such user");
    }
}
