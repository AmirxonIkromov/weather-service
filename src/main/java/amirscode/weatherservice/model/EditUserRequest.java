package amirscode.weatherservice.model;

import lombok.Data;

@Data
public class EditUserRequest {

    private boolean enabled;
    private String username;

}
