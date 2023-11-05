package com.vuongle.imagine.controllers.socket.v1;

import com.vuongle.imagine.constants.ChatType;
import com.vuongle.imagine.exceptions.DataFormatException;
import com.vuongle.imagine.exceptions.DataNotFoundException;
import com.vuongle.imagine.exceptions.UnAuthorizationException;
import com.vuongle.imagine.models.ChatMessage;
import com.vuongle.imagine.models.User;
import com.vuongle.imagine.models.embeded.Sender;
import com.vuongle.imagine.repositories.MessageRepository;
import org.bson.types.ObjectId;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;

import java.time.Instant;

@Controller
public class ChatController {

    private final MessageRepository messageRepository;

    public ChatController(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @MessageMapping("/chat/public")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {

        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) headerAccessor.getUser();

        if (authenticationToken == null) {
            chatMessage.setType(ChatType.PUBLIC);
            chatMessage.setSender(new Sender("anonymous", "anonymous"));
            chatMessage.setTimeStamp(Instant.now());
            return chatMessage;
//            throw new UnAuthorizationException("No authentication");
        }

        if (authenticationToken.getPrincipal() instanceof String username) {
            chatMessage.setType(ChatType.PUBLIC);
//            chatMessage.setSender(new Sender("anonymous-"+sessionId, "anonymous", sessionId));
            chatMessage.setSender(new Sender("anonymous-" + username, "anonymous-" + username));
            chatMessage.setTimeStamp(Instant.now());
        }

        if (authenticationToken.getPrincipal() instanceof User user) {
            chatMessage.setType(ChatType.PUBLIC);
            chatMessage.setSender(new Sender(user));
            chatMessage.setTimeStamp(Instant.now());
        }

        if (chatMessage.getSender() == null) {
            throw new DataNotFoundException("No sender");
        }

        messageRepository.save(chatMessage);

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
            throw new UnAuthorizationException("No authentication");
        }

        if (authenticationToken.getPrincipal() instanceof String username) {
            chatMessage.setType(ChatType.GROUP);
            chatMessage.setSender(new Sender("anonymous-" + username, "anonymous-" + username));
            chatMessage.setTimeStamp(Instant.now());
        }

        if (authenticationToken.getPrincipal() instanceof User user) {

            // Add username in web socket session
            chatMessage.setTimeStamp(Instant.now());
            chatMessage.setSender(new Sender(user));
            chatMessage.setConversationId(conversationId);
            chatMessage.setType(ChatType.GROUP);
        }

        if (chatMessage.getSender() == null) {
            throw new DataFormatException("No sender");
        }

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
