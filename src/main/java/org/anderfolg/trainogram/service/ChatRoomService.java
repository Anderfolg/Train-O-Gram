package org.anderfolg.trainogram.service;

import org.anderfolg.trainogram.entities.User;
import org.anderfolg.trainogram.entities.chatentities.ChatRoom;
import org.anderfolg.trainogram.exceptions.Status419UserException;
import org.anderfolg.trainogram.security.jwt.JwtUser;

import java.util.List;

public interface ChatRoomService {

    void deleteChatRoom(Long chatId, JwtUser jwtUser)
            throws Status419UserException;
    void deleteChatRoomById(Long chatId);

    void addUserToChatRoom(Long chatId, JwtUser jwtUser, String username)
            throws Status419UserException;
    Long createChatRoom( JwtUser jwtUser, List<String> usernames)
            throws Status419UserException;

    ChatRoom getChatRoomByChatIdAndSender(Long chatId, User sender);

    ChatRoom getChatRoomByChatId( Long chatId );
}
