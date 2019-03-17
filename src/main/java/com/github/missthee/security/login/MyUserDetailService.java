package com.github.missthee.security.login;

import com.github.missthee.security.utils.UserInfo;
import com.github.missthee.tool.ApplicationContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class MyUserDetailService implements UserDetailsService {
    private final UserInfo userInfo;

    @Autowired
    public MyUserDetailService(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return userInfo.loadUserByUsername(s);
    }
}
