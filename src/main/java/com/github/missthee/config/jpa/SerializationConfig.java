package com.github.missthee.config.jpa;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SerializationConfig {
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
//                .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)//结果忽略值为null值、{}值的属性。仅保留有值的属性
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);//当返回结果中属性可以为{}空对象。默认空对象会直接抛出异常。
    }
}
