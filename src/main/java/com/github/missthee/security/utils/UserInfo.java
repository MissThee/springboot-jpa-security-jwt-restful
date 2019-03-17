package com.github.missthee.security.utils;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserInfo {
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    UserDetails loadUserById(String id) throws UsernameNotFoundException;
}
