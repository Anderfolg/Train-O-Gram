package org.anderfolg.trainogram.entities.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
public class NotificationDto {
    private String message;
    private Long recipientId;
    private Long contentId;

}
