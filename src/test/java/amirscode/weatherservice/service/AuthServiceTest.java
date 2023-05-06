package amirscode.weatherservice.service;

import amirscode.weatherservice.config.JwtService;
import amirscode.weatherservice.entity.User;
import amirscode.weatherservice.enums.Role;
import amirscode.weatherservice.model.LoginDTO;
import amirscode.weatherservice.model.RegisterDTO;
import amirscode.weatherservice.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    @Test
    public void testRegister() {

        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setUsername("testuser");
        registerDTO.setPassword("testpassword");

        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setPassword(registerDTO.getPassword());
        user.setRole(Role.USER);

        when(passwordEncoder.encode(any())).thenReturn("encodedpassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtService.generateToken(any(User.class))).thenReturn("generatedtoken");

        ResponseEntity<?> responseEntity = authService.register(registerDTO);

        verify(userRepository, times(1)).save(any(User.class));
        verify(authenticationManager, times(1)).authenticate(
                ArgumentMatchers.any(UsernamePasswordAuthenticationToken.class)
        );
        verify(jwtService, times(1)).generateToken(any(User.class));

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("generatedtoken", responseEntity.getBody());
    }

    @Test
    public void testLogin() {

        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("testuser");
        loginDTO.setPassword("testpassword");

        User user = new User();
        user.setUsername(loginDTO.getUsername());
        user.setPassword("encodedpassword");
        user.setRole(Role.USER);

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(any(User.class))).thenReturn("generatedtoken");

        ResponseEntity<?> responseEntity = authService.login(loginDTO);

        verify(authenticationManager, times(1)).authenticate(
                ArgumentMatchers.any(UsernamePasswordAuthenticationToken.class)
        );
        verify(jwtService, times(1)).generateToken(any(User.class));

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("generatedtoken", responseEntity.getBody());
    }
}
