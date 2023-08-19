package com.vuongle.imagine.sockets;

import com.vuongle.imagine.models.ChatMessage;
import com.vuongle.imagine.models.Conversation;
import com.vuongle.imagine.models.User;
import com.vuongle.imagine.models.embeded.Sender;
import com.vuongle.imagine.repositories.ConversationRepository;
import com.vuongle.imagine.repositories.MessageRepository;
import com.vuongle.imagine.services.share.chat.ChatQueryService;
import com.vuongle.imagine.services.share.chat.impl.ChatQueryServiceImpl;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.security.Principal;
import java.time.Instant;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Component
public class WebSocketEventListener {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @Autowired
    private ChatQueryServiceImpl chatQueryService;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        logger.info("Received a new web socket connection");
    }

    @Value("${imagine.app.recent-message-limit}")
    private int recentMessageLimit;

    @EventListener
    public void handleSessionSubscribeEvent(SessionSubscribeEvent event) {

        GenericMessage<?> message = (GenericMessage<?>) event.getMessage();
        String simpDestination = (String) message.getHeaders().get("simpDestination");

        if (simpDestination == null) {
            return;
        }

        Principal principal = event.getUser();

        if (simpDestination.startsWith("/topic")) {

            String conversationStr = simpDestination.replace("/topic/", "");

            if (principal instanceof UsernamePasswordAuthenticationToken token) {

                if (token.getPrincipal() instanceof User user) {

                    if (conversationStr.equals("public")) {

                        List<ChatMessage> publicMessages = chatQueryService.findAllPublicMessages();
                        publicMessages.add(welcomeMessage(user.getUsername()));

                        messagingTemplate.convertAndSend(simpDestination, publicMessages);
                        return;
                    }

                    // get conversationId
                    ObjectId conversationId = new ObjectId(conversationStr);

                    // check if user in conversation
                    Conversation conversation = conversationRepository.findById(conversationId).orElseThrow(() -> new RuntimeException("Conversation not found"));

                    if (!conversation.getMembers().contains(user.getId())) {
                        return;
                    }

                    List<ChatMessage> messages = chatQueryService.findAllByConversationId(conversationId);

                    messagingTemplate.convertAndSend(simpDestination, messages);
                }
            } else {

                if (!conversationStr.equals("public")) {
                    return;
                }
                // anonymous join to public channel
                List<ChatMessage> publicMessages = chatQueryService.findAllPublicMessages();
                publicMessages.add(welcomeMessage("anonymous"));

                messagingTemplate.convertAndSend(simpDestination, publicMessages);
            }
        }
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        String username = (String) headerAccessor.getSessionAttributes().get("username");
        if (username != null) {
            logger.info("User Disconnected : " + username);

            ChatMessage chatMessage = new ChatMessage();
//            chatMessage.setType(ChatMessage.MessageType.LEAVE);
//            chatMessage.setSender(username);

            messagingTemplate.convertAndSend("/topic/public", chatMessage);
        }
    }

    private ChatMessage welcomeMessage(String username) {
        String welcomeMessage = String.format("Welcome %s to public channel", username);

        if (username.equals("anonymous")) {
            welcomeMessage += ", your chat message will not be saved";
        }

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setContent(welcomeMessage);
        chatMessage.setSender(new Sender("admin", "admin"));
        chatMessage.setTimeStamp(Instant.now());

        return chatMessage;
    }
}
