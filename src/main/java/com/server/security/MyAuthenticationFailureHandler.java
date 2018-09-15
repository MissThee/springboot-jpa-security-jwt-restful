package com.server.security;

import com.alibaba.fastjson.JSONObject;
import com.server.tool.ResponseOut;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component()
public class MyAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        log.info("登录失败: "+exception );
        JSONObject jO = new JSONObject();
        jO.put("result", false);
        jO.put("msg", exception.getMessage());
        ResponseOut.out200(response, jO);
    }
}
