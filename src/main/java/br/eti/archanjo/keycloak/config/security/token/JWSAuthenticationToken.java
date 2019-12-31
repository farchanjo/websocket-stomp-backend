package br.eti.archanjo.keycloak.config.security.token;

import lombok.*;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Objects;


@Getter
@ToString
public class JWSAuthenticationToken extends AbstractAuthenticationToken implements Authentication {


    private static final long serialVersionUID = -873657605051625392L;
    private String token;
    private User principal;

    public JWSAuthenticationToken(String token) {
        this(token, null, null);
    }

    public JWSAuthenticationToken(String token, User principal, Collection<GrantedAuthority> authorities) {
        super(authorities);
        this.token = token;
        this.principal = principal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        JWSAuthenticationToken that = (JWSAuthenticationToken) o;
        return principal.equals(that.principal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), principal);
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

}