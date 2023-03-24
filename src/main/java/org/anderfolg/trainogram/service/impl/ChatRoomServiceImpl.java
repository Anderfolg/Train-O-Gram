package org.anderfolg.trainogram.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.anderfolg.trainogram.entities.User;
import org.anderfolg.trainogram.entities.chatEntities.ChatRoom;
import org.anderfolg.trainogram.exceptions.Status419UserException;
import org.anderfolg.trainogram.repo.ChatRoomRepository;
import org.anderfolg.trainogram.security.jwt.JwtUser;
import org.anderfolg.trainogram.service.ChatRoomService;
import org.anderfolg.trainogram.service.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class ChatRoomServiceImpl implements ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    private final UserService userService;

    private void saveChatRoom(Long chatId,JwtUser jwtUser, List<String> usernames) throws Status419UserException {
        ArrayList<User> members = new ArrayList<>();
        members.add(userService.findUserById(jwtUser.id()));
        for (String username : usernames){
            members.add(userService.findByUsername(username));
        }

        for (User user : members){

            ArrayList<User> recipients = new ArrayList<>(members);
            recipients.remove(user);

            ChatRoom chatRoomSender = ChatRoom.builder()
                    .chatId(chatId)
                    .sender(user)
                    .recipients(recipients)
                    .build();
            chatRoomRepository.save(chatRoomSender);
        }
    }

    @Override
    public void deleteChatRoom(Long chatId, JwtUser jwtUser) throws Status419UserException {
        User user = userService.findUserById(jwtUser.id());
        ChatRoom chatRoom = chatRoomRepository.findChatRoomByChatIdAndSender(chatId, user);
        chatRoomRepository.delete(chatRoom);
    }

    @Override
    public void addUserToChatRoom(Long chatId, JwtUser jwtUser, String username) throws Status419UserException {
        User user = userService.findUserById(jwtUser.id());
        ChatRoom chatRoom = chatRoomRepository.findChatRoomByChatIdAndSender(chatId, user);
        User recipient = userService.findByUsername(username);
        chatRoom.getRecipients().add(recipient);
        chatRoomRepository.save(chatRoom);
    }

    // chat related functions
    
    @Override
    public Long createChatRoom( JwtUser jwtUser, List<String> usernames) throws Status419UserException {

        if (chatRoomRepository.findTopByOrderByChatIdDesc().isPresent()) {
            Long chatId = chatRoomRepository.findTopByOrderByChatIdDesc().get().getChatId() + 1L;
            saveChatRoom(chatId,jwtUser,usernames);
            log.info("chat ID is "+chatId);
            return chatId;
        }else {
            Long chatId = 0L;
            saveChatRoom(chatId,jwtUser,usernames);
            log.info("non present chats found so ID is "+chatId);
            return chatId;
        }}

    @Override
    public ChatRoom getChatRoomByChatIdAndSender(Long chatId, User sender) {
        return chatRoomRepository.findChatRoomByChatIdAndSender(chatId, sender);
    }
}
