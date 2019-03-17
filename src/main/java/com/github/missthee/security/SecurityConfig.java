package com.github.missthee.security;

import com.github.missthee.security.Handler.MyAccessDeniedHandler;
import com.github.missthee.security.Handler.MyAuthenticationEntryPoint;
import com.github.missthee.security.Handler.MyLogoutSuccessHandler;
import com.github.missthee.security.check.MyJWTVerificationFilter;
//import com.github.missthee.security.login.MyUsernamePasswordAuthenticationFilter;
import com.github.missthee.security.login.MyUserDetailService;
import com.github.missthee.security.login.MyUsernamePasswordAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.http.HttpSession;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(jsr250Enabled = true, prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService;

    @Autowired
    public SecurityConfig(MyUserDetailService userDetailsService) {
        this.userDetailsService = userDetailsService;
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
                .antMatchers("/files").permitAll()
                .antMatchers("/favicon.ico").permitAll()
                .and()
                .formLogin().loginProcessingUrl("/loginProcess").permitAll()
                .and()
                .logout().logoutUrl("/logoutProcess").invalidateHttpSession(true).logoutSuccessHandler(new MyLogoutSuccessHandler())
                .and()
                .addFilterAt(new MyUsernamePasswordAuthenticationFilter(authenticationManagerBean(), "/loginProcess"), UsernamePasswordAuthenticationFilter.class)
//                .addFilterAt(myJWTVerificationFilter, FilterSecurityInterceptor.class)
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
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
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);        //若不使用security保存的用户对象，则需要自行实现接口
        daoAuthenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder());  //密码使用BCryptPasswordEncoder()方法验证，因为这里使用了BCryptPasswordEncoder()方法验证。所以在注册用户的时候在接收前台明文密码之后也需要使用BCryptPasswordEncoder().encode(明文密码)方法加密密码,每次密码生成的均不同，但验证均可通过。
        daoAuthenticationProvider.setHideUserNotFoundExceptions(false);             //设置身份认证返回用户自定义消息。不设置则返回bad credentials
        return daoAuthenticationProvider;
    }

}
