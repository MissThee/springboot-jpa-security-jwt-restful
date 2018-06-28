package com.tenmax.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(jsr250Enabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsService myUserDetailsService;

    //    @Bean//测试时，未启用任何密码验证策略时使用
//    public static NoOpPasswordEncoder passwordEncoder() {
//        return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
//    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable() //此时既可以使用Get方法也可以使用Post方法提交
                .authorizeRequests()
//              .anyRequest()
                .antMatchers("/about").permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/provider/**").access("hasRole('ADMIN') and hasRole('PROVIDER')") //在hasRole和hasAnyRole中不需要加"ROLE_" 前缀
//						.antMatchers("/**").hasRole("USER")
                .and()
                .httpBasic()//security自带弹窗授权界面
//↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓以下三条一同使用，自定义登录界面，自定义处理登录请求的controller路由↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
//              .formLogin()//security自带页面授权界面
//              .loginPage("/login")//自定义登录界面controller路由，登录名参数必须被命名为username，密码参数必须被命名为password。
//              .loginProcessingUrl("/log") //登陆提交的处理url
//↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

//              .defaultSuccessUrl("/index") //指定登录成功后跳转的页面
//              .failureUrl("/login?error").permitAll() //指定登录失败后跳转的页面
                .and()
//                .rememberMe().tokenValiditySeconds(86400).key("mykey") //开启cookie储存用户信息，并设置有效期为1天，单位为s，指定cookie中的密钥
//                .rememberMeParameter("remember-me")//登陆时是否激活记住我功能的参数名字，在登陆页面有展示
//                .rememberMeCookieName("workspace")//cookies的名字，登陆后可以通过浏览器查看cookies名字
//                .and()
                .logout()
                .logoutUrl("/logout") //指定登出的url ,访问此地址即立即登出当前用户
                .addLogoutHandler((httpServletRequest, httpServletResponse, authentication) -> {
                    if (authentication != null) {
                        new SecurityContextLogoutHandler().logout(httpServletRequest, httpServletResponse, authentication);
                    }
                })
                .invalidateHttpSession(true)
//                .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))//在CSRF功能开启的情况下使用GET 去logout
                .logoutSuccessUrl("/logout1") //指定登场成功之后跳转的url，默认/login?logout
                .permitAll()
        ;
    }

    @Override
    //重写了configure参数为AuthenticationManagerBuilder的方法
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //并根据传入的AuthenticationManagerBuilder中的userDetailsService方法来接收我们自定义的认证方法。
        //且该方法必须要实现UserDetailsService这个接口。
        auth.userDetailsService(myUserDetailsService)
                //密码使用BCryptPasswordEncoder()方法验证，因为这里使用了BCryptPasswordEncoder()方法验证。所以在注册用户的时候在接收前台明文密码之后也需要使用BCryptPasswordEncoder().encode(明文密码)方法加密密码,每次密码生成的均不同，但验证均可通过。
                .passwordEncoder(new BCryptPasswordEncoder());
//        auth
//                .inMemoryAuthentication()
//                .withUser("a").password("123").roles("ADMIN", "PROVIDER", "USER")
//                .and()
//                .withUser("b").password("123").roles("PROVIDER")
//                .and()
//                .withUser("c").password("123").roles("USER")
//        ;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }
}
