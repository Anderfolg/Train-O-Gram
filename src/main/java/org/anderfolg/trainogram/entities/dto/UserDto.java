package org.anderfolg.trainogram.entities.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class UserDto {
    private String username;
    private String email;
    private String password;
}
