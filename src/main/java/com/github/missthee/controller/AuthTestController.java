package com.github.missthee.controller;

import com.github.missthee.tool.res.Res;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;

@RestController
@RequestMapping(value = "/auth")
public class AuthTestController {
    @RequestMapping(value = "/error")
    public Res error() {
        return Res.success(Integer.parseInt("zxc"));
    }

    @DenyAll
    @RequestMapping(value = "/deny")
    public Res deny() {
        return Res.success("all user cant get this!");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/admin")
    public Res admin() {
        return Res.success(SecurityContextHolder.getContext().getAuthentication(), "/admin");
    }

    @PreAuthorize("hasRole('ADMIN') and hasPermission(null,'PROVIDER1')")
    @RequestMapping(value = "/ap")
    public Res ap() {
        return Res.success(SecurityContextHolder.getContext().getAuthentication(), "/ap");
    }

    @PermitAll//不加注解与加@PermitAll相同
    @RequestMapping(value = "/all")
    public Res all() {
        return Res.success(SecurityContextHolder.getContext().getAuthentication(), "everyone can use");
    }

}
