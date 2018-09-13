package com.tenmax.controller;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Joiner;
import com.tenmax.db.primary.entity.SysPermission;
import com.tenmax.db.primary.entity.SysRole;
import com.tenmax.db.primary.entity.SysUser;
import com.tenmax.db.primary.service.SysUserService;
import com.tenmax.jwt.JavaJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RestController
public class LoginController {
    @Autowired
    SysUserService sysUserService;


    @PostMapping(value = "/login")
    public Object loginProcess(HttpServletResponse res, @RequestBody(required = false) JSONObject dataJO) {
        JSONObject jO = new JSONObject();
        String username;
        String password;

        if (dataJO.containsKey("username")) {
            username = dataJO.getString("username");
            if (StringUtils.isEmpty(username)) {
                jO.put("result", false);
                jO.put("msg", "用户名为空");
                return jO;
            }
        } else {
            jO.put("result", false);
            jO.put("msg", "无用户名字段");
            return jO;
        }

        if (dataJO.containsKey("password")) {
            password = dataJO.getString("password");
            if (StringUtils.isEmpty(password)) {
                jO.put("result", false);
                jO.put("msg", "密码为空");
                return jO;
            }
        } else {
            jO.put("result", false);
            jO.put("msg", "无密码字段");
            return jO;
        }
        SysUser sysUser = null;
        try {
            sysUser = sysUserService.selectUserByUsername(username);
        } catch (Exception e) {
            jO.put("result", false);
            jO.put("msg", "无此用户");
            return jO;
        }
        if (new BCryptPasswordEncoder().matches(password, sysUser.getPassword())) {
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("id", sysUser.getId());
            userMap.put("auth", Joiner.on(",").join(getAuthList(sysUser)));
            res.setHeader("Authorization", JavaJWT.createToken(userMap));
            jO.put("result", "true");
            jO.put("msg", "登录成功");
            return jO;
        } else {
            jO.put("result", false);
            jO.put("msg", "密码错误");
            return jO;
        }
    }

    @RequestMapping(value = "/loginPageRedirect")
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public Object loginPageRedirect() {
        JSONObject jO = new JSONObject();
        jO.put("msg", "未登录，跳转至登录页");
        return jO;
    }

    @PostMapping(value = "/exc")
    public Object exc() throws Exception {
        throw new Exception();
    }

    @PostMapping(value = "/exc1")
    public void exc1() throws Exception {
        Integer.parseInt("aa");
    }

    private List<String> getAuthList(SysUser sysUser) {
        List<String> list = new ArrayList<>(); //GrantedAuthority是security提供的权限类，
        Set<SysRole> roleList = sysUser.getRoleList();
        for (SysRole role : roleList) {
            list.add("ROLE_" + role.getRole());
            for (SysPermission permission : role.getPermissionList()) {
//                list.add(role.getRole() + ":" + permission.getPermission());
                list.add(permission.getPermission());
            }
        }
        //权限如果前缀是ROLE_，security就会认为这是个角色信息，而不是权限，例如ROLE_MENBER就是MENBER角色，CAN_SEND就是CAN_SEND权限
        return list;
    }
}
