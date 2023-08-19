package com.vuongle.imagine.controllers.socket.v1;

import com.vuongle.imagine.models.ChatMessage;
import com.vuongle.imagine.models.User;
import com.vuongle.imagine.repositories.MessageRepository;
import com.vuongle.imagine.utils.Context;
import org.bson.types.ObjectId;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.Instant;
import java.util.List;

@Controller
public class ChatController {

    private final MessageRepository messageRepository;

    public ChatController(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        return chatMessage;
    }

    @MessageMapping("/chat/{conversationId}")
    @SendTo("/topic/{conversationId}")
    public ChatMessage sendMessage(
            @DestinationVariable("conversationId") ObjectId conversationId,
            @Payload ChatMessage chatMessage,
            SimpMessageHeaderAccessor headerAccessor
    ) {
        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) headerAccessor.getUser();

        if (authenticationToken == null) {
            throw new RuntimeException("No user");
        }

        User user = (User) authenticationToken.getPrincipal();

        // Add username in web socket session
        chatMessage.setTimeStamp(Instant.now());
        chatMessage.setSenderId(user.getId());
        chatMessage.setConversationId(conversationId);
        messageRepository.save(chatMessage);
        return chatMessage;
    }

    @MessageMapping("/chat.sendStringMessage")
    @SendTo("/topic/public")
    public String sendMessage(@Payload String chatMessage) throws InterruptedException {
        Thread.sleep(200);
        return chatMessage;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage,
                               SimpMessageHeaderAccessor headerAccessor) {
        // Add username in web socket session
//        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }
}
