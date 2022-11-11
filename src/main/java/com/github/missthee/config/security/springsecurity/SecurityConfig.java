package com.github.missthee.config.security.springsecurity;

import com.github.missthee.config.security.springsecurity.filter.MyJWTVerificationFilter;
import com.github.missthee.config.security.springsecurity.filter.SecurityUserInfoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(jsr250Enabled = true, prePostEnabled = true, securedEnabled = true)
public class SecurityConfig {
    private final AuthenticationConfiguration authenticationConfiguration;

    public SecurityConfig(AuthenticationConfiguration authenticationConfiguration) {
        this.authenticationConfiguration = authenticationConfiguration;
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                //在Security的默认拦截器里，默认会开启CSRF处理，判断请求是否携带了_csrf校验值，如果没有就拒绝访问。在请求为(GET|HEAD|TRACE|OPTIONS)时，则不会开启。
                .csrf().disable() //这里关闭csrf。如果需求需要开启，需要要求前端配合修改来适配。
                .formLogin().disable()//（非必须）停用security的登录页。
                .httpBasic().disable()//（非必须）停用security的弹窗登录
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)//禁用session。因为依赖token获取用户信息的逻辑，此处可停用session
                .and()
                .build();
    }
}
//关于@EnableGlobalMethodSecurity注解（主要是开启各种注解）：
//@EnableGlobalMethodSecurity(securedEnabled=true)
//开启@Secured 注解过滤权限。如：@Secured({“ROLE_EMP”})、@Secured({“ROLE_EMP”, “ROLE_ROOT”})、@Secured({“ROLE_ROOT”})

//@EnableGlobalMethodSecurity(jsr250Enabled=true)
//开启@RolesAllowed 注解过滤权限

//@EnableGlobalMethodSecurity(prePostEnabled=true)
//使得表达式时间方法级别的安全性4个注解可用
//@PreAuthorize 在方法调用之前,基于表达式的计算结果来限制对方法的访问
//@PostAuthorize 允许方法调用,但是如果表达式计算结果为false,将抛出一个安全性异常
//@PostFilter 允许方法调用,但必须按照表达式来过滤方法的结果
//@PreFilter 允许方法调用,但必须在进入方法之前过滤输入值
