package com.github.missthee.controller;

import com.github.missthee.service.intef.SysUserService;
import com.github.missthee.tool.res.Res;
import com.github.missthee.db.primary.entity.SysUser;
import com.github.missthee.config.security.jwt.JavaJWT;
import com.github.missthee.vo.login.LoginVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
public class LoginController {
    private final SysUserService sysUserService;
    private final JavaJWT javaJWT;

    public LoginController(SysUserService sysUserService, JavaJWT javaJWT) {
        this.sysUserService = sysUserService;
        this.javaJWT = javaJWT;
    }

    @PostMapping(value = "/login")
    @ApiOperation(value = "登录", notes = "登录，header中获取token")
    public Res loginProcess(HttpServletResponse httpServletResponse, @RequestBody @Validated LoginVO loginVO) throws Exception {
        SysUser sysUser;
        try {
            sysUser = sysUserService.selectUserByUsername(loginVO.getUsername());
        } catch (Exception e) {
            return Res.failure("无此用户");
        }
        if (!new BCryptPasswordEncoder().matches(loginVO.getPassword(), sysUser.getPassword())) {
            return Res.failure("密码错误");
        }
        httpServletResponse.setHeader("Authorization", javaJWT.createToken(sysUser.getId(), 15));
        return Res.success("登录成功");
    }
}
