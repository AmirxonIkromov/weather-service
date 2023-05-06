package amirscode.weatherservice.mapper;

import amirscode.weatherservice.entity.City;
import amirscode.weatherservice.entity.User;
import amirscode.weatherservice.model.UserDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;


@Service
public class UserMapper implements Function<User, UserDTO> {

    @Override
    public UserDTO apply(User user) {
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.isEnabled()
        );
    }
}
