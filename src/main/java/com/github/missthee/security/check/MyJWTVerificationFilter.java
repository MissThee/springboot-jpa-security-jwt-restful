package com.github.missthee.security.check;

import com.github.missthee.db.primary.entity.SysPermission;
import com.github.missthee.db.primary.entity.SysRole;
import com.github.missthee.db.primary.entity.SysUser;
import com.github.missthee.db.primary.repository.UserRepository;
import com.github.missthee.jwt.JavaJWT;
import com.github.missthee.security.utils.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Component
public class MyJWTVerificationFilter extends OncePerRequestFilter {
    private final UserInfo userInfo;
    private final JavaJWT javaJWT;

    @Autowired
    public MyJWTVerificationFilter(JavaJWT javaJWT, UserInfo userInfo) {
        this.javaJWT = javaJWT;
        this.userInfo = userInfo;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
//        String token = httpServletRequest.getHeader("Authorization");
////        if (javaJWT.verifyToken(token)) {
////            String userId = javaJWT.getId(token);
////            UserDetails userDetails = userInfo.loadUserById(userId);
////            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
////            SecurityContext securityContext = new SecurityContextImpl();
////            securityContext.setAuthentication(authentication);
////            SecurityContextHolder.setContext(securityContext);
////
////        }
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        SessionRegistry sessionRegistry = new SessionRegistryImpl();
//        SessionInformation sessionInformation = sessionRegistry.getSessionInformation(token);
//        List<Object> allPrincipals = sessionRegistry.getAllPrincipals();
       filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    @Override
    public void destroy() {

    }


}
