package org.anderfolg.trainogram.entities.dto;

import lombok.*;
import org.anderfolg.trainogram.entities.Comment;

@AllArgsConstructor
@Setter
@Getter
public class CommentDto {
    private String comment;

    public CommentDto( Comment com) {
        this.setComment(com.getContent());
    }
}
