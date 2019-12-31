package br.eti.archanjo.keycloak.domain;

import br.eti.archanjo.keycloak.constants.TopicsConstants;
import br.eti.archanjo.keycloak.dto.MessageDTO;
import br.eti.archanjo.keycloak.dto.TotalUsersDTO;
import br.eti.archanjo.keycloak.dto.UserDTO;
import org.keycloak.representations.AccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Messages {
    private static final Logger logger = LoggerFactory.getLogger(Messages.class);

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final Users users;

    public Messages(SimpMessagingTemplate simpMessagingTemplate, Users users) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.users = users;
    }


    public void broadcast(MessageDTO messageDTO, AccessToken accessToken) {
        try {
            if (isValidMessage(messageDTO)) {
                addingTokenInformation(messageDTO, accessToken);
                if (isValidPublic(accessToken, messageDTO)) {
                    sendPublicChat(messageDTO);
                }
                if (isValidPrivate(messageDTO, accessToken)) {
                    sendIndividualMessage(messageDTO);
                }
            }
        } catch (Exception e) {
            logger.error("broadcast", e);
        }
    }

    private void sendIndividualMessage(MessageDTO messageDTO) {
        simpMessagingTemplate.convertAndSendToUser(messageDTO.getDestination(),
                TopicsConstants.PRIVATE_CHAT_TOPIC, messageDTO);
        simpMessagingTemplate.convertAndSendToUser(messageDTO.getSubject(),
                TopicsConstants.PRIVATE_CHAT_TOPIC, messageDTO);
    }

    private boolean isValidPrivate(MessageDTO messageDTO, AccessToken accessToken) {
        return (accessToken.isActive() && messageDTO.isIndividual() && messageDTO.getDestination() != null);
    }

    private boolean isValidPublic(AccessToken accessToken, MessageDTO messageDTO) {
        return (accessToken.isActive() &&
                !messageDTO.isIndividual());
    }

    private boolean isValidMessage(MessageDTO messageDTO) {
        return (messageDTO != null &&
                messageDTO.getMessage() != null &&
                !messageDTO.getMessage().isBlank() &&
                !messageDTO.getMessage().isEmpty());
    }

    private void addingTokenInformation(MessageDTO messageDTO, AccessToken accessToken) {
        messageDTO.setSender(accessToken.getName());
        messageDTO.setServerDate(new Date());
        messageDTO.setSubject(accessToken.getSubject());
        if (isValidPrivate(messageDTO, accessToken)) {
            users.getUsers().stream()
                    .filter(userDTO -> isValidUser(userDTO, messageDTO))
                    .findFirst()
                    .ifPresent(userDTO -> {
                        messageDTO.setDestination(userDTO.getSubject());
                        messageDTO.setDestinationName(userDTO.getName());
                    });
        }
    }

    private boolean isValidUser(UserDTO userDTO, MessageDTO messageDTO) {
        return ((userDTO != null && userDTO.getSubject() != null) &&
                (messageDTO != null && messageDTO.getDestination() != null) &&
                (userDTO.getSubject().equals(messageDTO.getDestination())));
    }

    private void sendPublicChat(MessageDTO messageDTO) {
        simpMessagingTemplate.convertAndSend(TopicsConstants.PUBLIC_CHAT_TOPIC, messageDTO);
    }

    public void notifyLoggedUsers() {
        List<UserDTO> usersLogged = users.getUsers();
        if (usersLogged != null) {
            simpMessagingTemplate.convertAndSend(TopicsConstants.TOTAL_USERS,
                    TotalUsersDTO.builder()
                            .total(usersLogged.size())
                            .users(usersLogged)
                            .build());
        }
    }
}
