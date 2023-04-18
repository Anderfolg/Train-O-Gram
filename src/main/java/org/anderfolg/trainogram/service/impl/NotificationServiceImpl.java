package org.anderfolg.trainogram.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.anderfolg.trainogram.entities.dto.NotificationDto;
import org.anderfolg.trainogram.entities.Notification;
import org.anderfolg.trainogram.entities.NotificationType;
import org.anderfolg.trainogram.entities.User;
import org.anderfolg.trainogram.repo.NotificationRepository;
import org.anderfolg.trainogram.service.NotificationService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;


    @Override
    public List<NotificationDto> listNotificationsByUser( User user ) {
        List<Notification> notificationList = notificationRepository.findAllByUser(user);
        List<NotificationDto> notifications = new ArrayList<>();
        for (Notification notification : notificationList){
            NotificationDto notificationDto = getDtoFromNotification(notification);
            notifications.add(notificationDto);
        }
        log.info("getting all notifications for user with ID:{}", user.getId());
        return notifications;
    }

    @Override
    public void createNotification(NotificationDto notificationDto, User user, NotificationType type) {
        Notification notification = getNotificationFromDto(notificationDto, user, type);
        notificationRepository.save(notification);
        log.info("creating notification for user with ID :{}", user.getId());
    }

    public NotificationDto getDtoFromNotification( Notification notification) {
        NotificationDto notificationDto = new NotificationDto();
        notificationDto.setMessage(notification.getMessage());
        notificationDto.setRecipientId(notification.getUser().getId());
        notificationDto.setContentId(notification.getContentId());
        return notificationDto;
    }

    public Notification getNotificationFromDto( NotificationDto notificationDto, User user, NotificationType type ) {
        Notification notification = new Notification();
        notification.setMessage(notificationDto.getMessage());
        notification.setUser(user);
        notification.setType(type);
        notification.setContentId(notificationDto.getContentId());
        return notification;
    }
}
