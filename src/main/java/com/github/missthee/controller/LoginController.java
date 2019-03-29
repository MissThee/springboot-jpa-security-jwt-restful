package com.github.missthee.controller;

import com.github.missthee.db.primary.service.intef.SysUserService;
import com.github.missthee.tool.Res;
import com.github.missthee.db.primary.entity.SysUser;
import com.github.missthee.config.security.jwt.JavaJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
public class LoginController {
    @Autowired
    SysUserService sysUserService;
    @Autowired
    JavaJWT javaJWT;

    @PostMapping(value = "/login")
    public Res loginProcess(HttpServletResponse httpServletResponse, @RequestBody(required = false) Map<String, Object> bJO) throws Exception {
        String username;
        String password;

        if (bJO.containsKey("username")) {
            username = (String) bJO.get("username");
            if (StringUtils.isEmpty(username)) {
                return Res.failure("用户名不能为空");
            }
        } else {
            return Res.failure("用户名不能为空");
        }
        if (bJO.containsKey("password")) {
            password = (String) bJO.get("password");
            if (StringUtils.isEmpty(password)) {
                return Res.failure("密码不能为空");
            }
        } else {
            return Res.failure("密码不能为空");
        }
        SysUser sysUser;
        try {
            sysUser = sysUserService.selectUserByUsername(username);
        } catch (Exception e) {
            return Res.failure("无此用户");
        }
        if (!new BCryptPasswordEncoder().matches(password, sysUser.getPassword())) {
            return Res.failure("密码错误");
        }
        httpServletResponse.setHeader("Authorization", javaJWT.createToken(sysUser.getId(), 15));
        return Res.success("登录成功");
    }
}
