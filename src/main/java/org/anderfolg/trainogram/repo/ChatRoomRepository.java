package org.anderfolg.trainogram.repo;

import org.anderfolg.trainogram.entities.User;
import org.anderfolg.trainogram.entities.chatEntities.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findTopByOrderByChatIdDesc();

    ChatRoom findChatRoomByChatIdAndSender(Long chatId, User sender);

    ChatRoom findChatRoomByChatId( Long chatId );

    void deleteChatRoomByChatId(Long chatId);
}
