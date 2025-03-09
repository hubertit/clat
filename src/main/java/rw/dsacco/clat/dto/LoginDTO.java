package rw.dsacco.clat.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class LoginDTO {
    private String email;
    private String password;

    public String getPassword() {
        return password;
    }
}
