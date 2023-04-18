package org.anderfolg.trainogram.repo;

import org.anderfolg.trainogram.entities.User;
import org.anderfolg.trainogram.entities.chatentities.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findTopByOrderByChatIdDesc();

    Optional<ChatRoom> findChatRoomByChatIdAndSender(Long chatId, User sender);

    Optional<ChatRoom> findChatRoomByChatId( Long chatId );

    void deleteChatRoomByChatId(Long chatId);
    void deleteChatRoomByChatIdAndSender(Long chatId, User sender);
}
