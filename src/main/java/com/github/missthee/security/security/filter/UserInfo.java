package com.github.missthee.security.check;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
@Transactional
public interface UserInfo {
    UserDetails loadUserById(String id) throws UsernameNotFoundException;
}
