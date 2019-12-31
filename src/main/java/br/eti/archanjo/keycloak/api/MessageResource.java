package br.eti.archanjo.keycloak.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(path = "/api/messages", produces = MediaType.APPLICATION_JSON_VALUE)
public class MessageResource {
    private static final Logger logger = LoggerFactory.getLogger(MessageResource.class);

    @RequestMapping(method = RequestMethod.GET)
    public List<String> getList() {
        SecurityContextHolder.getContext();
        return Arrays.asList("auhhdus", "usahuasha");
    }
}
