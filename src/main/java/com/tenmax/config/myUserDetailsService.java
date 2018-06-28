package com.tenmax.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class myUserDetailsService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        UserModel user = new UserModel();
        if (user == null) {
            throw new UsernameNotFoundException("User [" + userName + "] not found");
        }
        return new User(user.getUsername(), user.getPassword(), getAuthList(user));//返回包括权限角色的User(此User为security提供的实体类)给security;
    }

    private List<GrantedAuthority> getAuthList(UserModel user) {
        List<GrantedAuthority> list = new ArrayList<>(); //GrantedAuthority是security提供的权限类，
        for (String role : user.getRoles()) {
            //权限如果前缀是ROLE_，security就会认为这是个角色信息，而不是权限，例如ROLE_MENBER就是MENBER角色，CAN_SEND就是CAN_SEND权限
            list.add(new SimpleGrantedAuthority("ROLE_" + role));
        }
        return list;
    }
}
