package com.tenmax.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

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
                .csrf().disable(); //此时既可以使用Get方法也可以使用Post方法提交
        http
                .authorizeRequests()
               // .anyRequest() //所有请求

                .antMatchers("/about").permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/provider/**").access("hasRole('ADMIN') and hasRole('PROVIDER')") //在hasRole和hasAnyRole中不需要加"ROLE_" 前缀
                .antMatchers("/about").authenticated()
//						.antMatchers("/**").hasRole("USER")

                .and()
//                .httpBasic()//security自带弹窗授权界面,此方式使用 HTTP basic auth,登陆后每次访问会携带 “用户名:密码” 编码后的authorization头，使用此值来确定用户身份。明文携带账号密码，不安全
//                .addFilter(new JWTLoginFilter(authenticationManager()))
//                .addFilter(new JWTAuthenticationFilter(authenticationManager()))

//↓↓↓↓↓↓↓↓↓以下一同使用，自定义登录界面，自定义处理登录请求的controller路由,登录成功失败的返回结果↓↓↓↓↓↓↓↓↓
                .formLogin()//security自带页面授权界面

              // .loginPage("/login")//自定义登录界面controller路由，登录名参数默认被命名为username，密码参数必须被命名为password。
              // .loginProcessingUrl("/loginProcess")
//              .failureHandler(new AuthenticationFailureHandler() {
//                    @Override
//                    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
//                        httpServletResponse.setContentType("application/json;charset=utf-8");
//                        PrintWriter out = httpServletResponse.getWriter();
//                        StringBuffer sb = new StringBuffer();
//                        sb.append("{\"status\":\"error\",\"msg\":\"");
//                        if (e instanceof UsernameNotFoundException || e instanceof BadCredentialsException) {
//                            sb.append("用户名或密码输入错误，登录失败!");
//                        } else if (e instanceof DisabledException) {
//                            sb.append("账户被禁用，登录失败，请联系管理员!");
//                        } else {
//                            sb.append("登录失败!");
//                        }
//                        sb.append("\"}");
//                        out.write(sb.toString());
//                        out.flush();
//                        out.close();
//                    }
//                })
//                .successHandler(new AuthenticationSuccessHandler() {
//                    @Override
//                    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
//                        httpServletResponse.setContentType("application/json;charset=utf-8");
//                        PrintWriter out = httpServletResponse.getWriter();
//                        ObjectMapper objectMapper = new ObjectMapper();
//                        String s = "{\"status\":\"success\",\"msg\":" + objectMapper.writeValueAsString(HrUtils.getCurrentHr()) + "}";
//                        out.write(s);
//                        out.flush();
//                        out.close();
//                    }
//                })
//                .loginProcessingUrl("/log") //登陆提交的处理url
//↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

//              .defaultSuccessUrl("/index") //指定登录成功后跳转的页面
//              .failureUrl("/login?error").permitAll() //指定登录失败后跳转的页面
//                .and()
//                .rememberMe().tokenValiditySeconds(86400).key("mykey") //开启cookie储存用户信息，并设置有效期为1天，单位为s，指定cookie中的密钥
//                .rememberMeParameter("remember-me")//登陆时是否激活记住我功能的参数名字，在登陆页面有展示
//                .rememberMeCookieName("workspace")//cookies的名字，登陆后可以通过浏览器查看cookies名字
//                .and()
//                .logout( )
//                .logoutUrl("/logout") //指定登出的url ,访问此地址即立即登出当前用户
//                .addLogoutHandler((httpServletRequest, httpServletResponse, authentication) -> {
//                    if (authentication != null) {
//                        new SecurityContextLogoutHandler().logout(httpServletRequest, httpServletResponse, authentication);
//                    }
//                })
////              .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))//在CSRF功能开启的情况下使用GET 去logout
//                .logoutSuccessUrl("/logout1") //指定登场成功之后跳转的url，默认/login?logout
//                .permitAll()
        ;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //根据传入的AuthenticationManagerBuilder中的userDetailsService方法来接收我们自定义的认证方法。
        //且该方法必须要实现UserDetailsService这个接口。这个接口需返回添加了角色和权限的UserDetails对象或实现类对象
        auth.userDetailsService(myUserDetailsService)
                //密码使用BCryptPasswordEncoder()方法验证，因为这里使用了BCryptPasswordEncoder()方法验证。所以在注册用户的时候在接收前台明文密码之后也需要使用BCryptPasswordEncoder().encode(明文密码)方法加密密码,每次密码生成的均不同，但验证均可通过。
                .passwordEncoder(new BCryptPasswordEncoder());
//        直接添加用户信息于内存中
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
