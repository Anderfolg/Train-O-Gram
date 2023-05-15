package org.anderfolg.trainogram.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.anderfolg.trainogram.entities.ContentType;

@AllArgsConstructor
@Setter
@Getter
public class LikeDTO {
    private Long id;
    private Long userId;
    private Long contentId;
    private ContentType contentType;
}
