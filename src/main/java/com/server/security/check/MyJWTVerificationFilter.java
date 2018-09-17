package com.server.security.check;

import com.server.jwt.JavaJWT;
import com.server.tool.ResponseOut;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class MyJWTVerificationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String token = httpServletRequest.getHeader("Authorization");
        if (JavaJWT.verifyToken(token)) {
            String id = JavaJWT.getId(token);
            List<String> authList = JavaJWT.getAuthList(token);
            List<SimpleGrantedAuthority> list = new ArrayList<>();
            for (String auth : authList) {
                list.add(new SimpleGrantedAuthority(auth));
            }
            Authentication authentication = new UsernamePasswordAuthenticationToken(id, null, list);
            SecurityContext securityContext = new SecurityContextImpl();
            securityContext.setAuthentication(authentication);
            SecurityContextHolder.setContext(securityContext);
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    @Override
    public void destroy() {

    }

}
