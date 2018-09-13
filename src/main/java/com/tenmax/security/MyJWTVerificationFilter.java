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
public class MyJWTVerificationFilter extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String token = httpServletRequest.getHeader("Authorization");
        Authentication authentication;
        System.out.println(httpServletRequest.getRequestURI());
//       静态资源白名单路径暂时由此设置，以临时登录无实际权限用户身份获取

        if(httpServletRequest.getRequestURI().startsWith("/files/")||httpServletRequest.getRequestURI().equals("/favicon.ico")){
            authentication =new UsernamePasswordAuthenticationToken(null, null, null);
        }else {
            if (!JavaJWT.verifyToken(token)) {
                ResponseOut.out401(httpServletResponse);
                return;
            }
            String id = JavaJWT.getClaim(token, "id");
            String auth = JavaJWT.getClaim(token, "auth");

            authentication = new UsernamePasswordAuthenticationToken(id, null, authStr2List(auth));
        }
        SecurityContext securityContext = new SecurityContextImpl();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);

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
