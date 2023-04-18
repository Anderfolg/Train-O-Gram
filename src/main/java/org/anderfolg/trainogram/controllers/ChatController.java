package org.anderfolg.trainogram.controllers;

import lombok.RequiredArgsConstructor;
import org.anderfolg.trainogram.exceptions.Status419UserException;
import org.anderfolg.trainogram.security.jwt.JwtUser;
import org.anderfolg.trainogram.service.ChatRoomService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/chats")
public class ChatController {
    private final ChatRoomService chatRoomService;


    @PostMapping("/")
    public Long createChat( @RequestParam List<String> usernames, JwtUser jwtUser ) throws Status419UserException {
        return chatRoomService.createChatRoom(jwtUser,usernames);
    }

    @PostMapping("/users")
    public void addUserToChat( @RequestParam Long chatId, @RequestParam String username, JwtUser jwtUser ) throws Status419UserException {
        chatRoomService.addUserToChatRoom(chatId,jwtUser,username);
    }

    @DeleteMapping("/")
    public void deleteChat( @RequestParam Long chatId, JwtUser jwtUser ) throws Status419UserException {
        chatRoomService.deleteChatRoom(chatId,jwtUser);
    }

}
