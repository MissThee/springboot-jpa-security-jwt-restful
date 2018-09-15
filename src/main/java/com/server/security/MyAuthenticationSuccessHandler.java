package com.tenmax.security;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Joiner;
import com.tenmax.jwt.JavaJWT;
import com.tenmax.tool.ResponseOut;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component()
public class MyAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
//        request.getSession().setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
        log.info("登录成功");
        JSONObject jO = new JSONObject();
        jO.put("result", true);
        jO.put("msg", "success");
        jO.put("authentication", authentication);
        response.setHeader("Authorization", JavaJWT.createToken(getClaimMap(authentication)));
        ResponseOut.out200(response, jO);
    }

    private Map<String, Object> getClaimMap(Authentication authentication) {
        List<String> authList = new ArrayList<>();
        for (GrantedAuthority auth : authentication.getAuthorities()) {
            authList.add(auth.getAuthority());
        }
        String authStr = Joiner.on(",").join(authList);
        Map<String, Object> claimMap = new HashMap<>();
        claimMap.put("id", authentication.getName());
        claimMap.put("auth", authStr);
        return claimMap;
    }
}
