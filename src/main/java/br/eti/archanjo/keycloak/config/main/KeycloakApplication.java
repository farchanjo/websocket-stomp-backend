package br.eti.archanjo.keycloak.config.main;

import br.eti.archanjo.keycloak.config.PropertiesConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication(scanBasePackages = "br.eti.archanjo.keycloak")
@EnableConfigurationProperties(PropertiesConfig.class)
public class KeycloakApplication {

    public static void main(String[] args) {
        SpringApplication.run(KeycloakApplication.class, args);
    }

}
