package br.eti.archanjo.keycloak.messaging;

import br.eti.archanjo.keycloak.constants.TopicsConstants;
import br.eti.archanjo.keycloak.domain.Messages;
import br.eti.archanjo.keycloak.dto.MessageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

@Controller
public class MessageMessagingResource extends MessagingGenerics {
    private static final Logger logger = LoggerFactory.getLogger(MessageMessagingResource.class);

    private final Messages messages;

    public MessageMessagingResource(Messages messages) {
        this.messages = messages;
    }

    @MessageMapping(TopicsConstants.MESSAGES)
    private void messages(MessageDTO messageDTO) throws Exception {
        messages.broadcast(messageDTO, getToken(SecurityContextHolder.getContext()));
    }
}
