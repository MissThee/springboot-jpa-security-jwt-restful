package com.github.missthee.config.security.jwt;


import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.io.Serial;
import java.util.Collection;
@Component
public class JWTAuthenticationToken extends AbstractAuthenticationToken {

    @Serial
    private static final long serialVersionUID = 570L;
    private final String username;
    private final String password;

    public JWTAuthenticationToken() {
        super(null);
        this.username = null;
        this.password = null;
        setAuthenticated(false);
    }

    public JWTAuthenticationToken(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.username = username;
        this.password = password;
        super.setAuthenticated(true);
    }

    public Object getCredentials() {
        return this.password;
    }

    public Object getPrincipal() {
        return this.username;
    }
}
