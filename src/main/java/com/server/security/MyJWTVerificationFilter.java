package com.server.security;

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
        System.out.println(httpServletRequest.getRequestURI());
//       静态资源白名单路径暂时由此设置，以临时登录无实际权限用户身份获取
//        if (httpServletRequest.getRequestURI().startsWith("/files/") || httpServletRequest.getRequestURI().equals("/favicon.ico")) {
//            authentication = new UsernamePasswordAuthenticationToken(null, null, null);
//        } else {
        if ( JavaJWT.verifyToken(token)) {
            String id = JavaJWT.getClaim(token, "id");
            String auth = JavaJWT.getClaim(token, "auth");
            Authentication authentication = new UsernamePasswordAuthenticationToken(id, null, authStr2List(auth));
            SecurityContext securityContext = new SecurityContextImpl();
            securityContext.setAuthentication(authentication);
            SecurityContextHolder.setContext(securityContext);
        }
        SecurityContext context = SecurityContextHolder.getContext();
//        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
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
