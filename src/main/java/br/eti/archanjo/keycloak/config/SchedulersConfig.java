package br.eti.archanjo.keycloak.config;

import br.eti.archanjo.keycloak.domain.Messages;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class SchedulersConfig {

    private final Messages messages;

    public SchedulersConfig(Messages messages) {
        this.messages = messages;
    }

    @Scheduled(fixedDelay = 1000)
    private void sendUsersLogged() {
        messages.notifyLoggedUsers();
    }
}
