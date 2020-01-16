package com.github.missthee.controller;

import com.github.missthee.tool.res.Res;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;

@RestController
public class AuthTestController {
    @RequestMapping(value = "/errorFunction")
    public Res errorFunction() {
        return Res.success(Integer.parseInt("zxc"));
    }

    @DenyAll
    @RequestMapping(value = "/deny")
    public Res deny() {
        return Res.success("all user cant get this!");
    }

    @PreAuthorize("hasRole('ADMIN') and hasRole('PROVIDER1')")
    @RequestMapping(value = "/auth")
    public Res auth() {
        return Res.success(SecurityContextHolder.getContext().getAuthentication(), "/auth");
    }

    @PermitAll//不加注解与加@PermitAll相同
    @RequestMapping(value = "/all")
    public Res all() {
        return Res.success(SecurityContextHolder.getContext().getAuthentication(), "everyone can use");
    }

}
