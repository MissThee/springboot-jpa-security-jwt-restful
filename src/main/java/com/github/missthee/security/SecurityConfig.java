package com.server.security;

import com.server.security.check.MyAccessDeniedHandler;
import com.server.security.check.MyAuthenticationEntryPoint;
import com.server.security.check.MyJWTVerificationFilter;
import com.server.security.login.MyUsernamePasswordAuthenticationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(jsr250Enabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsService myUserDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MyJWTVerificationFilter myJWTVerificationFilter;
    private static String loginProcessingUrl = "/loginProcess";

    @Value("${custom-config.security.login-processing-url}")
    public void setRootPath(String a) {
        loginProcessingUrl = a;
    }

    public SecurityConfig(UserDetailsService myUserDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder, MyJWTVerificationFilter myJWTVerificationFilter ) {
        this.myUserDetailsService = myUserDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.myJWTVerificationFilter = myJWTVerificationFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable(); //既可以使用Get方法也可以使用Post方法提交
        http
                .exceptionHandling().authenticationEntryPoint(new MyAuthenticationEntryPoint())
                .and()
                .exceptionHandling().accessDeniedHandler(new MyAccessDeniedHandler())
                .and()
                .authorizeRequests()
//                .antMatchers("/files").permitAll()
                .and()
                .formLogin()
//                .loginPage("/loginPageRedirect") //security需要登录时，跳转此路由。返回前端需要登陆的信息
                .loginProcessingUrl(loginProcessingUrl)  // 自定义的登录接口
                .permitAll()
                .and()
                .addFilterAt(myUsernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterAt(myJWTVerificationFilter, FilterSecurityInterceptor.class)//不再此处设置替换过滤器，此过滤器也生效
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        ;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        //根据传入的AuthenticationManagerBuilder中的userDetailsService方法来接收我们自定义的认证方法。
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Override
    public void configure(WebSecurity web) {
    }

    DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(myUserDetailsService); //且该方法必须要实现UserDetailsService这个接口。这个接口需返回添加了角色和权限的UserDetails对象或实现类对象
        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder);//密码使用BCryptPasswordEncoder()方法验证，因为这里使用了BCryptPasswordEncoder()方法验证。所以在注册用户的时候在接收前台明文密码之后也需要使用BCryptPasswordEncoder().encode(明文密码)方法加密密码,每次密码生成的均不同，但验证均可通过。
        daoAuthenticationProvider.setHideUserNotFoundExceptions(false);//设置身份认证返回用户自定义消息。不设置则返回bad credentials
        return daoAuthenticationProvider;
    }

    UsernamePasswordAuthenticationFilter myUsernamePasswordAuthenticationFilter() throws Exception {
        MyUsernamePasswordAuthenticationFilter filter = new MyUsernamePasswordAuthenticationFilter();
        filter.setFilterProcessesUrl(loginProcessingUrl);
        filter.setAuthenticationManager(authenticationManagerBean());
        return filter;
    }

}
