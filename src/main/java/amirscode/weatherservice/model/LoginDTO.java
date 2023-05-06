package amirscode.weatherservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDTO {

    @NonNull
    private String username;
    @NonNull
    private String password;
}
