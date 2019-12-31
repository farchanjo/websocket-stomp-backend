package br.eti.archanjo.keycloak.config;


import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;

@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
@ConfigurationProperties(prefix = "config")
public class PropertiesConfig {
    private RabbitMq rabbitMq = new RabbitMq();

    @AllArgsConstructor(access = AccessLevel.PUBLIC)
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
    @Getter
    @Setter
    @ToString
    @EqualsAndHashCode
    @Builder
    public static class RabbitMq {
        private String username;
        private String password;
        private String host;
        private int port;
    }
}
