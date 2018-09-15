package com.server.security;

import com.alibaba.fastjson.JSON;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
//重写获取前端提交的用户名、密码方法，以支持获取application/json格式参数
public class MyUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    MyUsernamePasswordAuthenticationFilter() {    //构造函数，设置通过身份验证与未通过执行方法
        setAuthenticationFailureHandler(new MyAuthenticationFailureHandler());
        setAuthenticationSuccessHandler(new MyAuthenticationSuccessHandler());
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username;
        String password;
        //获取前端请求中提交的用户名、密码；支持两种方式获取，application/json或security原生的参数获取
        if (request.getContentType().equals(MediaType.APPLICATION_JSON_UTF8_VALUE) || request.getContentType().equals(MediaType.APPLICATION_JSON_VALUE)) {
            Map<String, String> auth = new HashMap<>();
            try {
                auth = JSON.parseObject(request.getInputStream(), Map.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            username = auth.getOrDefault(getUsernameParameter(), "");
            password = auth.getOrDefault(getPasswordParameter(), "");
        } else {
            username = this.obtainUsername(request);
            password = this.obtainPassword(request);
            if (username == null) {
                username = "";
            }

            if (password == null) {
                password = "";
            }
        }

        username = username.trim();
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, password);
//        sessionRegistry.registerNewSession(request.getSession().getId(), authRequest.getPrincipal());
        this.setDetails(request, usernamePasswordAuthenticationToken);
        return this.getAuthenticationManager().authenticate(usernamePasswordAuthenticationToken);
    }
}
