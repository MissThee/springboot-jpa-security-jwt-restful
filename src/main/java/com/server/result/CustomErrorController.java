package com.server.result;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RestController
public class CustomErrorController implements ErrorController {
    @Override
    public String getErrorPath() {
        return "error";
    }

    //自定义404返回，及@RestControllerAdvice不拦截的异常
    @RequestMapping(value = "/error")
    public Object error(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JSONObject jO = new JSONObject();
        HttpStatus httpStatus = HttpStatus.valueOf(response.getStatus());
        jO.put("msg", "/error: " + httpStatus.getReasonPhrase()+". ");
        ErrorLogPrinter.logOutPut(request);
        return new ResponseEntity<>(jO, httpStatus);//ResponseEntity 可动态指定返回状态码
    }

//    private void ResUnauthorized(ServletResponse servletResponse) throws IOException {
//        HttpServletResponse response = (HttpServletResponse) servletResponse;
//        response.setHeader("Content-Type", "application/json;charset=UTF-8");
//        response.setHeader("Access-Control-Allow-Origin", "*");
//        //把返回值输出到客户端
//        ServletOutputStream out = response.getOutputStream();
//        out.write(ciphertext.getBytes());
//        out.flush();
//    }

//    private static byte[] getRequestBytes(HttpServletResponse response) throws IOException {
//        int contentLength = response.getBufferSize();
//        if (contentLength < 0) {
//            return null;
//        }
//        byte buffer[] = new byte[contentLength];
//        for (int i = 0; i < contentLength; ) {
//            int readLen = response.getOutputStream().println(buffer, i, contentLength - i);
//            if (readLen == -1) {
//                break;
//            }
//            i += readLen;
//        }
//        return buffer;
//    }
//
//    private static String getRequestStr(HttpServletResponse response) throws IOException {
//        byte buffer[] = getRequestBytes(response);
//        String charEncoding = response.getCharacterEncoding();
//        if (charEncoding == null) {
//            charEncoding = "UTF-8";
//        }
//        return new String(buffer, charEncoding);
//    }
}
