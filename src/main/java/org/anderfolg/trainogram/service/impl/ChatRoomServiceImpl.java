package org.anderfolg.trainogram.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.anderfolg.trainogram.entities.User;
import org.anderfolg.trainogram.entities.chatentities.ChatRoom;
import org.anderfolg.trainogram.exceptions.Status419UserException;
import org.anderfolg.trainogram.exceptions.Status436DoesntExistException;
import org.anderfolg.trainogram.repo.ChatRoomRepository;
import org.anderfolg.trainogram.security.jwt.JwtUser;
import org.anderfolg.trainogram.service.ChatRoomService;
import org.anderfolg.trainogram.service.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        if (chatRoomRepository.findChatRoomByChatIdAndSender(chatId, user).isPresent() ){
            chatRoomRepository.deleteChatRoomByChatIdAndSender(chatId, user);
        }
    else throw new Status436DoesntExistException(chatId + " is not present");
    }

    @Override
    public void deleteChatRoomById( Long chatId ) {
        chatRoomRepository.deleteChatRoomByChatId(chatId);
    }

    @Override
    public void addUserToChatRoom(Long chatId, JwtUser jwtUser, String username) throws Status419UserException {
        User user = userService.findUserById(jwtUser.id());
        ChatRoom chatRoom = getChatRoomByChatIdAndSender(chatId, user);
        User recipient = userService.findByUsername(username);
        chatRoom.getRecipients().add(recipient);
        chatRoomRepository.save(chatRoom);
    }

    // chat related functions

    @Override
    public Long createChatRoom(JwtUser jwtUser, List<String> usernames) throws Status419UserException {

        Optional<ChatRoom> lastChatRoom = chatRoomRepository.findTopByOrderByChatIdDesc();
        long chatId;
        if (lastChatRoom.isPresent()) {
            chatId = lastChatRoom.get().getChatId() + 1L;
            saveChatRoom(chatId, jwtUser, usernames);
            log.info("chat ID is " + chatId);
        } else {
            chatId = 0L;
            saveChatRoom(chatId, jwtUser, usernames);
            log.info("non present chats found so ID is " + chatId);
        }
        return chatId;
    }


    @Override
    public ChatRoom getChatRoomByChatIdAndSender(Long chatId, User sender) {
        return chatRoomRepository.findChatRoomByChatIdAndSender(chatId, sender)
                .orElseThrow(()-> new Status436DoesntExistException("ChatRoom with id " + chatId + " doesn't exist"));
    }

    @Override
    public ChatRoom getChatRoomByChatId( Long chatId ) {
        return chatRoomRepository.findChatRoomByChatId(chatId)
                .orElseThrow(()-> new Status436DoesntExistException("ChatRoom with id " + chatId + " doesn't exist"));
    }
}
