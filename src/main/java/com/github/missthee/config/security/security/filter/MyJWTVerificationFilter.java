package com.github.missthee.config.security.security.filter;

import com.github.missthee.config.security.jwt.JavaJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class MyJWTVerificationFilter extends OncePerRequestFilter {
    private final UserInfo userInfo;
    private final JavaJWT javaJWT;

    @Autowired
    public MyJWTVerificationFilter(JavaJWT javaJWT, UserInfo userInfo) {
        this.javaJWT = javaJWT;
        this.userInfo = userInfo;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String token = httpServletRequest.getHeader("Authorization");
        Authentication authentication;
        if (javaJWT.verifyToken(token)) {
            String userId = javaJWT.getId(token);
            UserDetails userDetails = userInfo.loadUserById(userId);
            authentication = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
        } else {
            authentication = new UsernamePasswordAuthenticationToken(null, null);
        }
        SecurityContext securityContext = new SecurityContextImpl();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    @Override
    public void destroy() {

    }
}
