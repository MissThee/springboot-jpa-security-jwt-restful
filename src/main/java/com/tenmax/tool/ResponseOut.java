package com.tenmax.tool;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class ResponseOut {
    //把返回值输出到客户端
    private static void out(HttpServletResponse response, JSONObject bodyJO) throws IOException {
        response.setHeader("Content-Type", "application/json;charset=UTF-8");
        ServletOutputStream out = response.getOutputStream();
        out.write(JSON.toJSONString(bodyJO).getBytes());
        out.flush();
    }

    public static void out200(HttpServletResponse response, JSONObject bodyJO) throws IOException {
        out(response, bodyJO);
    }

    private static void out(HttpServletResponse response, int code) throws IOException {
        HttpStatus httpStatus = HttpStatus.valueOf(code);
        response.setStatus(httpStatus.value());
        JSONObject jO = new JSONObject() {{
            put("_msg", httpStatus.getReasonPhrase());
        }};
        out(response, jO);
    }

    public static void out401(HttpServletResponse response) throws IOException {
        out(response, 401);
    }

    public static void out403(HttpServletResponse response) throws IOException {
        out(response, 403);
    }

    public static void out500(HttpServletResponse response) throws IOException {
        out(response, 500);
    }

    public static void out404(HttpServletResponse response) throws IOException {
        out(response, 404);
    }
//    public static void out(HttpServletResponse response, Object bodyJO, Map<String, String> headerMap) throws IOException {
//        if (headerMap != null) {
//            for (String key : headerMap.keySet()) {
//                response.setHeader("key", headerMap.get(key));
//            }
//        }
//        response.setHeader("Content-Type", "application/json;charset=UTF-8");
//        ServletOutputStream out = response.getOutputStream();
//        out.write(JSON.toJSONString(bodyJO).getBytes());
//        out.flush();
//    }
}
