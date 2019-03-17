package com.server.security.check;

import com.server.db.primary.entity.SysPermission;
import com.server.db.primary.entity.SysRole;
import com.server.db.primary.entity.SysUser;
import com.server.db.primary.repository.UserRepository;
import com.server.jwt.JavaJWT;
import com.server.tool.ResponseOut;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class MyJWTVerificationFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;

    @Autowired
    public MyJWTVerificationFilter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String token = httpServletRequest.getHeader("Authorization");
        if (JavaJWT.verifyToken(token)) {
            String userId = JavaJWT.getId(token);
            List<String> authList = getAuthList(userId);
            List<SimpleGrantedAuthority> list = new ArrayList<>();
            for (String auth : authList) {
                list.add(new SimpleGrantedAuthority(auth));
            }
            Authentication authentication = new UsernamePasswordAuthenticationToken(userId, null, list);
            SecurityContext securityContext = new SecurityContextImpl();
            securityContext.setAuthentication(authentication);
            SecurityContextHolder.setContext(securityContext);
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    @Override
    public void destroy() {

    }

    private List<String> getAuthList(String userId) {
        SysUser sysUser = userRepository.findFirstById(userId);
        List<String> list = new ArrayList<>(); //GrantedAuthority是security提供的权限类，
        Set<SysRole> roleList = sysUser.getRoleList();
        for (SysRole role : roleList) {
            list.add("ROLE_" + role.getRole());
            for (SysPermission permission : role.getPermissionList()) {
                list.add(permission.getPermission());
            }
        }
        //权限如果前缀是ROLE_，security就会认为这是个角色信息，而不是权限，例如ROLE_MENBER就是MENBER角色，CAN_SEND就是CAN_SEND权限
        return list;
    }
}
