package com.server.result;


import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
//    //security访问无权限接口异常(AccessDeniedException不能在此拦截，会导致登录与未登录均在AccessDeniedException被拦截，无法区分)
//    @ExceptionHandler(AccessDeniedException.class)
//    @ResponseBody
//    @ResponseStatus(code = HttpStatus.FORBIDDEN)
//    public Object accessDeniedExceptionHandler(HttpServletRequest request, Exception e) {
//        ErrorLogPrinter.logOutPut(request, e);
//        JSONObject jO = new JSONObject();
//        jO.put("msg", HttpStatus.FORBIDDEN.getReasonPhrase());
//        return jO;
//    }
//
//    @ExceptionHandler(AuthenticationException.class)
//    @ResponseBody
//    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
//    public Object authenticationExceptionHandler(HttpServletRequest request, Exception e) {
//        ErrorLogPrinter.logOutPut(request, e);
//        JSONObject jO = new JSONObject();
//        jO.put("msg", HttpStatus.UNAUTHORIZED.getReasonPhrase());
//        return jO;
//    }


//    //运行时所有异常
//    @ExceptionHandler(Exception.class)
//    @ResponseBody
//    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
//    public Object exceptionHandler(HttpServletRequest request, Exception e) {
//        e.printStackTrace();
//        ErrorLogPrinter.logOutPut(request, e);
//        JSONObject jO = new JSONObject();
//        jO.put("msg", "exceptionHandler: " + e);
//        return jO;
//    }
}

