package com.server.security.login;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Joiner;
import com.server.jwt.JavaJWT;
import com.server.tool.ResponseOut;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component()
public class MyAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        JSONObject jO = new JSONObject();
        jO.put("result", true);
        jO.put("msg", "success");

        String id = authentication.getName();
        List<String> authList = new ArrayList<>();
        for (GrantedAuthority auth : authentication.getAuthorities()) {
            authList.add(auth.getAuthority());
        }
        response.setHeader("Authorization", JavaJWT.createToken(id, authList));

        ResponseOut.out200(response, jO);
    }


}
