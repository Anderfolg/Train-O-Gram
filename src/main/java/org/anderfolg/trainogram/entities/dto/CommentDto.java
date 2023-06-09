package org.anderfolg.trainogram.entities.dto;

import lombok.*;
import org.anderfolg.trainogram.entities.ContentType;

import java.util.Set;

@AllArgsConstructor
@Setter
@Getter
public class CommentDto {
    private Long id;
    private String content;
    private Long userId;
    private Long postId;
    private Set<LikeDTO> likes;
    private ContentType type;

}
