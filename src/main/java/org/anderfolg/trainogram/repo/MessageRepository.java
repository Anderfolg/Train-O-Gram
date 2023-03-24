package org.anderfolg.trainogram.repo;

import org.anderfolg.trainogram.entities.chatEntities.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message,Long> {
    List<Message> findAllByChatIdAndAndRecipientId( Long chatId, Long recipientId);
}
