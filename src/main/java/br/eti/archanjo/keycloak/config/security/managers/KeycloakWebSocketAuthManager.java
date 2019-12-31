package br.eti.archanjo.keycloak.config.security.managers;

import br.eti.archanjo.keycloak.config.security.token.JWSAuthenticationToken;
import br.eti.archanjo.keycloak.utils.TokenUtils;
import org.keycloak.common.VerificationException;
import org.keycloak.representations.AccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class KeycloakWebSocketAuthManager implements AuthenticationManager {
    private static final Logger logger = LoggerFactory.getLogger(KeycloakWebSocketAuthManager.class);

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JWSAuthenticationToken token = TokenUtils.getToken(authentication);
        String tokenString = TokenUtils.getCredentials(token);
        try {
            AccessToken accessToken = TokenUtils.getAccessToken(tokenString);
            token = new JWSAuthenticationToken(tokenString, TokenUtils.getUser(tokenString, accessToken),
                    TokenUtils.getAuthorities(accessToken));
            token.setAuthenticated(true);
        } catch (VerificationException e) {
            logger.debug(String.format("Exception authenticating the token %s", tokenString), e);
            throw new BadCredentialsException(String.format("Invalid token: %s", e.getMessage()));
        }
        return token;
    }


}