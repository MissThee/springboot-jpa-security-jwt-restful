package com.tenmax.result;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
    private final static Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);
    //运行时所有异常
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result excptoinHandler(HttpServletRequest request, Exception e) {
        logger.info("!------------------%ERROR%-------------------!");
        logger.info("URL    : {}", request.getRequestURL().toString());
        logger.info("METHOD : {}", request.getMethod());
        logger.info("URI    : {}", request.getRequestURI());
        logger.info("IP     : {}", request.getRemoteAddr());
        logger.info("PARAMS : {}", request.getQueryString());
        logger.info("ERROR  : {}",e.toString());
        logger.info("!------------------#ERROR#-------------------!");
        return Result.error(e.toString());
    }

//    private HttpStatus getStatus(HttpServletRequest request) {
//        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
//        if (statusCode == null) {
//            return HttpStatus.INTERNAL_SERVER_ERROR;
//        }
//        return HttpStatus.valueOf(statusCode);
//    }
}

