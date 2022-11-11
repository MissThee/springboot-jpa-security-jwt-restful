package com.github.missthee.config.security.springsecurity.filter;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;

public interface SecurityUserInfoUtil {
    UserDetails loadUserById(Object id) throws BadCredentialsException;
    //生成security身份对象时不再从session中获取，而是从token中获取用户信息，自行构建security身份对象。
    //此为获取UserDetails的接口，需用户自行构建UserDetails，用以生成security身份对象。
    //因每个系统获取用户权限的方式不同，此方法作为一个接口可供开发人员自行实现。若找不到任何实现类，运行会报错
}
