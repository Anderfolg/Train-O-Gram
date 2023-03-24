package org.anderfolg.trainogram.repo;

import org.anderfolg.trainogram.entities.Notification;
import org.anderfolg.trainogram.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByUser( User user);
}
