package org.anderfolg.trainogram.entities.DTO;

import lombok.*;
import org.anderfolg.trainogram.entities.User;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@RequiredArgsConstructor
public class PostDto {
    private String imageName;
    private String imageUrl;
    private @NotNull String description;
    private User user;
}
