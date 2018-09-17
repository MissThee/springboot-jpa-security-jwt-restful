package com.server.security.login;

import com.server.db.primary.entity.SysPermission;
import com.server.db.primary.entity.SysRole;
import com.server.db.primary.entity.SysUser;
import com.server.db.primary.service.SysUserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class MyUserDetailsService implements UserDetailsService {

    private final SysUserService sysUserService;

    public MyUserDetailsService(SysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String inputUsername) {
        SysUser sysUser = sysUserService.selectUserByUsername(inputUsername);
        if (sysUser == null) {
            throw new UsernameNotFoundException("User not found [" + inputUsername + "]", new Throwable());
        }
        return new User(sysUser.getId().toString(), sysUser.getPassword(), getAuthList(sysUser));//返回包括权限角色的User(此User为security提供的实体类)给security;
    }

    private List<GrantedAuthority> getAuthList(SysUser sysUser) {
        List<GrantedAuthority> list = new ArrayList<>(); //GrantedAuthority是security提供的权限类，
        Set<SysRole> roleList = sysUser.getRoleList();
        for (SysRole role : roleList) {
            list.add(new SimpleGrantedAuthority("ROLE_" + role.getRole()));
            for (SysPermission permission : role.getPermissionList()) {
                list.add(new SimpleGrantedAuthority(role.getRole() + ":" + permission.getPermission()));
            }
        }
        //权限如果前缀是ROLE_，security就会认为这是个角色信息，而不是权限，例如ROLE_MENBER就是MENBER角色，CAN_SEND就是CAN_SEND权限
        return list;
    }
}
