package com.vuongle.imagine.config;

import com.vuongle.imagine.sockets.WebSocketAuthenticatorService;
import com.vuongle.imagine.utils.JwtUtils;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private static final String USERNAME_HEADER = "login";
    private static final String PASSWORD_HEADER = "passcode";

    private final WebSocketAuthenticatorService webSocketAuthenticatorService;

    private final JwtUtils jwtUtils;

    private final UserDetailsService userDetailsService;

    public WebSocketConfig(WebSocketAuthenticatorService webSocketAuthenticatorService, JwtUtils jwtUtils, UserDetailsService userDetailsService) {
        this.webSocketAuthenticatorService = webSocketAuthenticatorService;
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(@NotNull Message<?> message, @NotNull MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

                if (Objects.isNull(accessor)) {
                    return message;
                }

                // Authentication user
                if (StompCommand.CONNECT == accessor.getCommand()) {

                    // login bằng token
                    String jwtToken = accessor.getFirstNativeHeader("Authorization");

                    if (StringUtils.isEmpty(jwtToken)) {

                        String username = accessor.getFirstNativeHeader("username");

                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
//                            accessor.getSessionId()
                                username,
                            null,
                            new ArrayList<>(Collections.singleton(new SimpleGrantedAuthority("ANONYMOUS")))
                        );
                        accessor.setUser(authentication);
                        return message;
                    }

                    String username = jwtUtils.getUserNameFromJwtToken(jwtToken);

                    if (Objects.nonNull(username) && SecurityContextHolder.getContext().getAuthentication() == null) {

                        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                        if (jwtUtils.isTokenValid(jwtToken, userDetails)) {
                            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities());
                            authentication.setDetails(new WebAuthenticationDetailsSource());
                            accessor.setUser(authentication);
                        }
                    }

                    // login bằng username/password
//                    String username = accessor.getFirstNativeHeader(USERNAME_HEADER);
//                    String passcode = accessor.getPasscode();
//
//
//                    UsernamePasswordAuthenticationToken user = webSocketAuthenticatorService.getAuthenticatedOrFail(username, passcode);
//                    accessor.setUser(user);
                }

                return message;

//                return ChannelInterceptor.super.preSend(message, channel);
            }
        });
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*");
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableSimpleBroker("/topic");
        registry.setUserDestinationPrefix("/user");
    }
}
