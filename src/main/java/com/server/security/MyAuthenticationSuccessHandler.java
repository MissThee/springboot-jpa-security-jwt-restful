package com.server.security;

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
