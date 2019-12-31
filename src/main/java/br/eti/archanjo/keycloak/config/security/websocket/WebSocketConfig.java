package br.eti.archanjo.keycloak.config.security.websocket;

import br.eti.archanjo.keycloak.config.PropertiesConfig;
import br.eti.archanjo.keycloak.config.security.interceptors.WebsocketAuthInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketConfig.class);

    private final PropertiesConfig config;
    private final WebsocketAuthInterceptor websocketAuthInterceptor;

    public WebSocketConfig(WebsocketAuthInterceptor websocketAuthInterceptor,
                           PropertiesConfig config) {
        this.websocketAuthInterceptor = websocketAuthInterceptor;
        this.config = config;
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry
                                               registry) {
        registry.addEndpoint("/socket/ws")
                .setAllowedOrigins("*")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        /*
         * Using RabbitMQ With stomp broker.
         */
        registry.enableStompBrokerRelay("/topic/", "/queue/", "/public")
                .setClientLogin(config.getRabbitMq().getUsername())
                .setClientPasscode(config.getRabbitMq().getPassword())
                .setSystemLogin(config.getRabbitMq().getUsername())
                .setSystemPasscode(config.getRabbitMq().getPassword())
                .setRelayHost(config.getRabbitMq().getHost())
                .setRelayPort(config.getRabbitMq().getPort());
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(websocketAuthInterceptor);
    }
}