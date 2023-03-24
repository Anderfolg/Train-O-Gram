package org.anderfolg.trainogram.entities.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.anderfolg.trainogram.entities.User;

import java.io.Serializable;
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class UserDto {
    private String username;
    private String email;
    private String password;
}
