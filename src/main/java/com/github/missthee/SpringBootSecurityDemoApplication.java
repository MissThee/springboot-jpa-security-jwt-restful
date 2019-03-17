package com.github.missthee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
//@EnableSwagger2
public class SpringBootSecurityDemoApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootSecurityDemoApplication.class, args);
    }
}
