package br.eti.archanjo.keycloak.utils;

import br.eti.archanjo.keycloak.config.security.token.JWSAuthenticationToken;
import org.keycloak.TokenVerifier;
import org.keycloak.common.VerificationException;
import org.keycloak.representations.AccessToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

public class TokenUtils {
    public static String getCredentials(JWSAuthenticationToken token) {
        return String.valueOf(token.getCredentials());
    }

    public static JWSAuthenticationToken getToken(Principal principal) {
        return (JWSAuthenticationToken) principal;
    }

    public static JWSAuthenticationToken getToken(Authentication authentication) {
        return (JWSAuthenticationToken) authentication;
    }

    public static AccessToken getAccessToken(String token) throws VerificationException {
        return TokenVerifier.create(token, AccessToken.class).getToken();
    }

    public static List<GrantedAuthority> getAuthorities(AccessToken accessToken) {
        return accessToken.getRealmAccess().getRoles()
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    public static User getUser(String token, AccessToken accessToken) {
        return new User(accessToken.getSubject(), token, getAuthorities(accessToken));
    }
}
