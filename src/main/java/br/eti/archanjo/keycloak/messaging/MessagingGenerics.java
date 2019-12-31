package br.eti.archanjo.keycloak.messaging;

import br.eti.archanjo.keycloak.config.security.token.JWSAuthenticationToken;
import br.eti.archanjo.keycloak.utils.TokenUtils;
import org.keycloak.common.VerificationException;
import org.keycloak.representations.AccessToken;
import org.springframework.security.core.context.SecurityContext;

public class MessagingGenerics {
    protected AccessToken getToken(SecurityContext securityContext) throws VerificationException {
        JWSAuthenticationToken jwsToken = TokenUtils.getToken(securityContext.getAuthentication());
        return TokenUtils.getAccessToken(TokenUtils.getCredentials(jwsToken));
    }
}
