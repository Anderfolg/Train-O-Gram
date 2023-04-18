package org.anderfolg.trainogram.service;

import org.anderfolg.trainogram.entities.dto.NotificationDto;
import org.anderfolg.trainogram.entities.NotificationType;
import org.anderfolg.trainogram.entities.User;

import java.util.List;

public interface NotificationService {
    List<NotificationDto> listNotificationsByUser(User user);

    void createNotification(NotificationDto notificationDto, User user, NotificationType type);
}
