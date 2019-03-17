package com.github.missthee.security.login;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.missthee.security.utils.UserInfo;
import com.github.missthee.tool.ApplicationContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

//重写获取前端提交的用户名、密码方法，以支持获取application/json格式参数
public class MyUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    public MyUsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager, String loginUrl) {
        setAuthenticationFailureHandler((request, response, exception) -> {
            JSONObject jO = new JSONObject();
            jO.put("result", false);
            jO.put("msg", exception.getMessage());
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            ServletOutputStream out = response.getOutputStream();
            out.write(JSON.toJSONString(jO).getBytes());
            out.flush();
        });
        setAuthenticationSuccessHandler((request, response, authentication) -> {
            JSONObject jO = new JSONObject();
            jO.put("result", true);
            jO.put("msg", authentication);
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            response.setHeader("Authorization", ((WebAuthenticationDetails) authentication.getDetails()).getSessionId());
            ServletOutputStream out = response.getOutputStream();
            out.write(JSON.toJSONString(jO).getBytes());
            out.flush();
        });
        setAuthenticationManager(authenticationManager);
        setFilterProcessesUrl(loginUrl);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = null;
        String password = null;
        //获取前端请求中提交的用户名、密码；支持两种方式获取，application/json或security原生的参数获取
        try {
            Map<String, String> auth = JSON.parseObject(request.getInputStream(), Map.class);
            username = auth.getOrDefault(getUsernameParameter(), null);
            password = auth.getOrDefault(getPasswordParameter(), null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (username == null || password == null) {
            username = this.obtainUsername(request);
            password = this.obtainPassword(request);
            if (username == null) {
                username = "";
            }

            if (password == null) {
                password = "";
            }
        }

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, password, getSimpleGrantedAuthoritySet(username));
        this.setDetails(request, usernamePasswordAuthenticationToken);
        return this.getAuthenticationManager().authenticate(usernamePasswordAuthenticationToken);
    }

    private Collection<? extends GrantedAuthority> getSimpleGrantedAuthoritySet(String username) {
        UserInfo userInfo = ApplicationContextHolder.getBean(UserInfo.class);
        return userInfo.loadUserByUsername(username).getAuthorities();
    }
}
