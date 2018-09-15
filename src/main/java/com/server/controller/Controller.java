package com.server.controller;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

@RestController
public class Controller {

    @PostMapping(value = "/admin")
    public JSONObject admin() {
        JSONObject jO = new JSONObject();
        jO.put("result", "/admin");
        return jO;
    }

    @PostMapping(value = "/provider")
    public JSONObject provider() {
        JSONObject jO = new JSONObject();
        jO.put("result", "/provider");
        return jO;
    }

    @PostMapping(value = "/user")
    public JSONObject user() {
        JSONObject jO = new JSONObject();
        jO.put("result", "/user");
        return jO;
    }

    @PostMapping(value = "/about")
    public JSONObject about() {
        JSONObject jO = new JSONObject();
        jO.put("result", "/about");
        return jO;
    }

    //==================== method annotation ====================
    @DenyAll
    @PostMapping(value = "/deny")
    public JSONObject deny() {
        System.out.println("all user cant get this!");
        JSONObject jO = new JSONObject();
        jO.put("result", "all user cant get this!");
        return jO;
    }

    @RolesAllowed({"ADMIN", "PROVIDER"})
    @PostMapping(value = "/adminAT")
    public JSONObject adminAT() {
        System.out.println("!!authentication!!:" + SecurityContextHolder.getContext().getAuthentication());
        JSONObject jO = new JSONObject();
        jO.put("result", "/adminAT");
        jO.put("result-getAuthentication", SecurityContextHolder.getContext().getAuthentication());
        return jO;
    }

    @RolesAllowed({"ADMIN1", "PROVIDER1"})
    @PostMapping(value = "/adminAT1")
    public JSONObject adminAT1() {
        System.out.println("!!authentication!!:" + SecurityContextHolder.getContext().getAuthentication());
        JSONObject jO = new JSONObject();
        jO.put("result", "/adminAT1");
        jO.put("result-getAuthentication", SecurityContextHolder.getContext().getAuthentication());
        return jO;
    }

    @PermitAll
    @PostMapping(value = "/all")
    public JSONObject all() {
        JSONObject jO = new JSONObject();
        jO.put("result", "everyone can login !");
        return jO;
    }

    @PermitAll
    @PostMapping(value = "/error1")
    public JSONObject error() {
        JSONObject jO = new JSONObject();
        jO.put("result", "error page");
        return jO;
    }

    @PostMapping(value = "/logout1")
    public JSONObject logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            System.out.println("!!auth-BEFORE!!:" + SecurityContextHolder.getContext().getAuthentication());
            new SecurityContextLogoutHandler().logout(request, response, auth);
            System.out.println("!!auth-AFTER!!:" + SecurityContextHolder.getContext().getAuthentication());
        }
        JSONObject jO = new JSONObject();
        jO.put("result", "LogoutSuccess。default redirect:/login?logout");
        return jO;
    }


}
