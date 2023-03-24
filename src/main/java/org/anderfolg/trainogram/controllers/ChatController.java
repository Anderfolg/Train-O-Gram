package org.anderfolg.trainogram.controllers;

import lombok.RequiredArgsConstructor;
import org.anderfolg.trainogram.exceptions.Status419UserException;
import org.anderfolg.trainogram.security.jwt.JwtUser;
import org.anderfolg.trainogram.service.ChatRoomService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/chat-controller/")
public class ChatController {
    private final ChatRoomService chatRoomService;


    @PostMapping("/create-chat")
    public Long createChat( @RequestParam List<String> usernames, JwtUser jwtUser ) throws Status419UserException {
        return chatRoomService.createChatRoom(jwtUser,usernames);
    }

    @PostMapping("/add-user")
    public void addUserToChat( @RequestParam Long chatId, @RequestParam String username, JwtUser jwtUser ) throws Status419UserException {
        chatRoomService.addUserToChatRoom(chatId,jwtUser,username);
    }

    @DeleteMapping("/delete-chat")
    public void deleteChat( @RequestParam Long chatId, JwtUser jwtUser ) throws Status419UserException {
        chatRoomService.deleteChatRoom(chatId,jwtUser);
    }

}
