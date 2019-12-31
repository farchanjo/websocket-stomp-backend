package br.eti.archanjo.keycloak.domain;

import br.eti.archanjo.keycloak.dto.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Users {
    private static final Logger logger = LoggerFactory.getLogger(Users.class);

    private static final String PRINCIPAL_KEY = "users:logged:";
    private final RedisTemplate<String, List<UserDTO>> redisUserDTOTemplate;

    public Users(RedisTemplate<String, List<UserDTO>> redisUserDTOTemplate) {
        this.redisUserDTOTemplate = redisUserDTOTemplate;
    }

    public void addUser(UserDTO userDTO) {
        if (userDTO == null)
            return;
        List<UserDTO> users = getUsers();
        if (users == null)
            users = new ArrayList<>();
        if (!isAlreadyExists(users, userDTO))
            users.add(userDTO);
        redisUserDTOTemplate.opsForValue().set(PRINCIPAL_KEY, users);
        logger.debug(String.format("%s users on redis", users.size()));
    }

    private boolean isAlreadyExists(List<UserDTO> users, UserDTO userDTO) {
        return users.stream().anyMatch(user -> user.equals(userDTO));
    }

    public List<UserDTO> getUsers() {
        return redisUserDTOTemplate.opsForValue().get(PRINCIPAL_KEY);
    }

    public void removeUser(UserDTO userDTO) {
        List<UserDTO> users = getUsers();
        if (users == null)
            return;
        users.removeIf(user -> user.equals(userDTO));
        redisUserDTOTemplate.opsForValue().set(PRINCIPAL_KEY, users);
        logger.debug(String.format("%s removed from redis", userDTO.getName()));
    }
}
