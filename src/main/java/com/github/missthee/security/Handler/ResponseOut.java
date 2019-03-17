package com.github.missthee.security.Handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.http.HttpStatus;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ResponseOut {
    public static void out(HttpServletResponse httpServletResponse, JSONObject bodyJO, HttpStatus httpStatus) throws IOException {
        httpServletResponse.setHeader("Access-Control-Expose-Headers", "Authorization");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", "*");
        httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpServletResponse.setHeader("Content-Type", "application/json;charset=UTF-8");
        httpServletResponse.setStatus(httpStatus.value());
        ServletOutputStream outputStream = httpServletResponse.getOutputStream();
        outputStream.write(JSON.toJSONString(bodyJO).getBytes());
        outputStream.flush();
    }
}
