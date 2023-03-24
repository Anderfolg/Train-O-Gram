package org.anderfolg.trainogram.controllers;

import org.anderfolg.trainogram.entities.User;
import org.anderfolg.trainogram.entities.chatEntities.ChatRoom;
import org.anderfolg.trainogram.entities.chatEntities.Message;
import org.anderfolg.trainogram.repo.MessageRepository;
import org.anderfolg.trainogram.service.ChatRoomService;
import org.anderfolg.trainogram.service.UserService;
import org.anderfolg.trainogram.websocket.MessageDecoder;
import org.anderfolg.trainogram.websocket.MessageEncoder;
import org.anderfolg.trainogram.websocket.SpringContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
@ServerEndpoint(value = "/chat/{chatId}", decoders = MessageDecoder.class, encoders = MessageEncoder.class)
public class ChatEndpoint {
    private Session session;

    private User currentUser;


    private final ChatRoomService chatRoomService;

    private final UserService userService;

    private final MessageRepository messageRepository;

    private ChatRoom currentChatRoom;


    private static final Set<ChatEndpoint> chatEndpoints = new CopyOnWriteArraySet<>();
    private static HashMap<String, Long> users = new HashMap<>();

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
    public void onClose() {

        chatEndpoints.remove(this);

    }

    @OnError
    public void onError(Throwable throwable) {
        System.out.println(throwable.getMessage());
        // TODO: 28/2/23 Do error handling here
    }

    private static void broadcast(Message  message) {

        // TODO: 27/2/23 local var synced?
        chatEndpoints.forEach(endpoint -> {
            synchronized (endpoint) {
                if (endpoint.currentChatRoom.getChatId().equals(message.getChatId())) {
                    if (message.getRecipient().equals(endpoint.currentUser)){
                        try {
                            endpoint.session.getBasicRemote().
                                    sendObject(message);
                        } catch (IOException | EncodeException e) {
                            e.printStackTrace();
                        }
                    }}
            }
        });
    }
}
