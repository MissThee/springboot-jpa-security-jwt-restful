package com.tenmax.security;

import com.alibaba.fastjson.JSONObject;
import com.tenmax.jwt.JavaJWT;
import com.tenmax.tool.ResponseOut;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class MyJWTVerificationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        String token = httpServletRequest.getHeader("Authorization");

        if (!JavaJWT.verifyToken(token)) {
            ResponseOut.out401(httpServletResponse);
            return;
        }
        String id = JavaJWT.getClaim(token, "id");
        String auth = JavaJWT.getClaim(token, "auth");

        Authentication authentication = new UsernamePasswordAuthenticationToken(id, null, authStr2List(auth));

        SecurityContext securityContext = new SecurityContextImpl();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);

        filterChain.doFilter(servletRequest, servletResponse);

    }

    @Override
    public void destroy() {

    }

    private List<SimpleGrantedAuthority> authStr2List(String auths) {
        List<SimpleGrantedAuthority> list = new ArrayList<>();
        for (String auth : auths.split(",")) {
            list.add(new SimpleGrantedAuthority(auth));
        }
        return list;
    }
}
