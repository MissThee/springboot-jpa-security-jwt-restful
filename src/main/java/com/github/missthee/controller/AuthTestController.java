package com.github.missthee.controller;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

@RestController
public class Controller {
    @RequestMapping(value = "/errorFunction")
    public JSONObject errorFunction() {
        JSONObject jO = new JSONObject();
        Integer.parseInt("zxc");
        jO.put("result", "/admin");
        return jO;
    }

    //==================== method annotation ====================
    @DenyAll
    @RequestMapping(value = "/deny")
    public JSONObject deny() {
        System.out.println("all user cant get this!");
        JSONObject jO = new JSONObject();
        jO.put("result", "all user cant get this!");
        return jO;
    }

    @PreAuthorize("hasRole('ADMIN') and hasRole('PROVIDER')")
    @RequestMapping(value = "/auth")
    public JSONObject auth() {
        JSONObject jO = new JSONObject();
        jO.put("result", "/auth");
        jO.put("result-getAuthentication", SecurityContextHolder.getContext().getAuthentication());
        return jO;
    }

    @PermitAll//不加注解与加@PermitAll相同
    @RequestMapping(value = "/all")
    public JSONObject all() {
        JSONObject jO = new JSONObject();
        jO.put("result", "everyone can use");
        jO.put("result-getAuthentication", SecurityContextHolder.getContext().getAuthentication());
        return jO;
    }

    @RequestMapping(value = "/error1")
    public JSONObject error() {
        JSONObject jO = new JSONObject();
        jO.put("result", "error page");
        return jO;
    }
}
