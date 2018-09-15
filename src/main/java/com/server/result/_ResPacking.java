package com.server.result;

import com.alibaba.fastjson.JSONObject;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

//将Controller中返回值默认使用Result.ok()打包
@ControllerAdvice
public class _ResPacking implements ResponseBodyAdvice<Object> {
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

//        if (body instanceof JSONObject) {
//            return body;
//        }

        if (body instanceof Byte || body instanceof Short || body instanceof Integer || body instanceof Long || body instanceof Double || body instanceof Float || body instanceof Character || body instanceof Boolean) {
            JSONObject jO = new JSONObject();
            jO.put("msg", "");
            jO.put("result", body);
            return jO;
        } else if (body instanceof String) {
            JSONObject jO = new JSONObject();
            jO.put("msg", "");
            jO.put("result", body);
            return jO;
        }

        return body;
    }
//            return new JSONObject("result", body);

}
