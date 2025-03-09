package rw.dsacco.clat.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private String name;
    private String phone;
    private String email;
    private String role;
    private String password;
    private String status;
}
