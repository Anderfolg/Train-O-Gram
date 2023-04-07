package org.anderfolg.trainogram.controllers;

import lombok.extern.slf4j.Slf4j;
import org.anderfolg.trainogram.entities.User;
import org.anderfolg.trainogram.entities.chatEntities.ChatRoom;
import org.anderfolg.trainogram.entities.chatEntities.Message;
import org.anderfolg.trainogram.repo.MessageRepository;
import org.anderfolg.trainogram.service.ChatRoomService;
import org.anderfolg.trainogram.service.UserService;
import org.anderfolg.trainogram.websocket.MessageDecoder;
import org.anderfolg.trainogram.websocket.MessageEncoder;
import org.anderfolg.trainogram.websocket.SpringContext;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
@ServerEndpoint(value = "/chat/{chatId}", decoders = MessageDecoder.class, encoders = MessageEncoder.class)
@Slf4j
public class ChatEndpoint {
    private Session session;

    private User currentUser;

    private final ChatRoomService chatRoomService;

    private final UserService userService;

    private final MessageRepository messageRepository;

    private ChatRoom currentChatRoom;


    private static final Set<ChatEndpoint> chatEndpoints = new CopyOnWriteArraySet<>();
    private static final HashMap<String, Long> users = new HashMap<>();

    public ChatEndpoint(){
        this.chatRoomService =  SpringContext.getApplicationContext().getBean(ChatRoomService.class);
        this.userService =  SpringContext.getApplicationContext().getBean(UserService.class);
        this.messageRepository =  SpringContext.getApplicationContext().getBean(MessageRepository.class);
    }


    @OnOpen
    public void onOpen(Session session, @PathParam("chatId") Long chatId) {

        this.session = session;
        this.currentUser = userService.findByUsername(session.getUserPrincipal().getName());
        this.currentChatRoom = chatRoomService.getChatRoomByChatIdAndSender(chatId,currentUser);
        users.put(session.getId(), chatId);
        chatEndpoints.add(this);

        for (Message message : messageRepository.findAllByChatIdAndAndRecipientId(chatId,currentUser.getId())){
            message.setViewed(true);
            messageRepository.save(message);
            broadcast(message);
        }
    }

    @OnMessage
    public void onMessage(Session session, String content,@PathParam("chatId") Long chatId) {

        ArrayList<User> recipients = new ArrayList<>(currentChatRoom.getRecipients());

        for (User user : recipients){
            Message message = Message.builder()
                    .chatId(chatId)
                    .text(content)
                    .viewed(false)
                    .author(userService.findByUsername(session.getUserPrincipal().getName()))
                    .recipient(user)
                    .build();
            messageRepository.save(message);
            broadcast(message);

        }
    }

    @OnClose
    //  TODO (Bogdan O.) 7/4/23: instead of multiple nesting - better use Optionals for getting objects and handle "not found" exceptions
    public void onClose() {
        Long chatId = users.remove(session.getId());
        chatEndpoints.remove(this);
        if (chatId != null) {
            ChatRoom chatRoom = chatRoomService.getChatRoomByChatId(chatId);
            if (chatRoom != null) {
                List<User> remainingUsers = chatRoom.getRecipients().stream()
                        .filter(user -> chatEndpoints.stream()
                                .anyMatch(endpoint -> endpoint.currentUser.equals(user))).toList();
                if (remainingUsers.isEmpty()) {
                    messageRepository.deleteAllByChatId(chatId);
                    chatRoomService.deleteChatRoomById(chatId);
                }
            }
        }
    }

    @OnError
    //  TODO (Bogdan O.) 7/4/23: throw exceptions on every major error
    public void onError(Throwable throwable) {
        log.error("Error in ChatEndpoint: " + throwable.getMessage());



    }

    private static void broadcast(Message  message) {
        Object lock = new Object();

        chatEndpoints.forEach(endpoint -> {
            synchronized (lock) {
                if (endpoint.currentChatRoom.getChatId().equals(message.getChatId()) && (message.getRecipient().equals(endpoint.currentUser))){
                        try {
                            endpoint.session.getBasicRemote().
                                    sendObject(message);
                        } catch (IOException | EncodeException e) {
                            e.printStackTrace();
                        }
                    }
            }
        });
    }
}
