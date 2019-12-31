package br.eti.archanjo.keycloak.config.security.interceptors;

import br.eti.archanjo.keycloak.config.security.managers.KeycloakWebSocketAuthManager;
import br.eti.archanjo.keycloak.config.security.token.JWSAuthenticationToken;
import br.eti.archanjo.keycloak.domain.Messages;
import br.eti.archanjo.keycloak.domain.Users;
import br.eti.archanjo.keycloak.dto.UserDTO;
import br.eti.archanjo.keycloak.utils.TokenUtils;
import org.keycloak.common.VerificationException;
import org.keycloak.representations.AccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.Optional;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class WebsocketAuthInterceptor implements ChannelInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(WebsocketAuthInterceptor.class);
    private final KeycloakWebSocketAuthManager keycloakWebSocketAuthManager;

    private final Messages messages;
    private final Users users;

    public WebsocketAuthInterceptor(KeycloakWebSocketAuthManager keycloakWebSocketAuthManager,
                                    Users users, @Lazy Messages messages) {
        this.keycloakWebSocketAuthManager = keycloakWebSocketAuthManager;
        this.users = users;
        this.messages = messages;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor != null) {
            autorization(accessor);
            updateUser(accessor);
            removeUser(accessor);
        }
        return message;
    }

    private void removeUser(StompHeaderAccessor accessor) {
        if (StompCommand.DISCONNECT.equals(accessor.getCommand())) {
            UserDTO dto = getUserDTO(accessor.getUser());
            if (dto != null) {
                users.removeUser(dto);
                messages.notifyLoggedUsers();
            }
        }
    }

    private void updateUser(StompHeaderAccessor accessor) {
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            UserDTO dto = getUserDTO(accessor.getUser());
            if (dto != null) {
                users.addUser(dto);
                messages.notifyLoggedUsers();
            }
        }
    }

    private void autorization(StompHeaderAccessor accessor) {
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            Optional.ofNullable(accessor.getNativeHeader("Authorization")).ifPresent(ah -> {
                String bearerToken = ah.get(0).replace("Bearer ", "");
                logger.debug(String.format("Bearer Token has been found: %s", bearerToken));
                JWSAuthenticationToken token = (JWSAuthenticationToken) keycloakWebSocketAuthManager
                        .authenticate(new JWSAuthenticationToken(bearerToken));
                logger.debug(String.format("User %s auth has been created", token.getPrincipal()));
                accessor.setUser(token);
            });
        }
    }

    private UserDTO getUserDTO(Principal principal) {
        try {
            AccessToken token = TokenUtils.getAccessToken(TokenUtils.getCredentials(TokenUtils.getToken(principal)));
            return UserDTO.builder()
                    .subject(token.getSubject())
                    .name(token.getName())
                    .build();
        } catch (VerificationException e) {
            logger.error("getUserDTO", e);
        }
        return null;
    }
}
