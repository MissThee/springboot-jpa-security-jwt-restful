package com.tenmax.result;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

//将Controller中返回值默认使用Result.ok()打包
@ControllerAdvice
public class ResultPacking implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {
//指定路径不对返回结果进行打包
//        String requestPath = request.getURI().getPath();
//        if (!requestPath.startsWith("/ug")) {
//            return body;
//        }
        if (body instanceof Result) {
            return body;
        } else {
            return Result.ok(body);
        }
    }
}
