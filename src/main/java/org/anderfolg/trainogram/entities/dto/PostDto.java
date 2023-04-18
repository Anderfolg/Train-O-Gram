package org.anderfolg.trainogram.entities.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.anderfolg.trainogram.entities.User;

import javax.validation.constraints.NotNull;

@Data
@RequiredArgsConstructor
public class PostDto {
    private String imageName;
    private String imageUrl;
    private @NotNull String description;
    @JsonIgnoreProperties({"password"})
    private User user;
}
