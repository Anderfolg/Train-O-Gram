package org.anderfolg.trainogram.entities.dto;

import lombok.Data;

@Data
public class AuthRequestDTO {
    private String username;
    private String password;
}
