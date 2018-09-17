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

    @RequestMapping(value = "/admin")
    public JSONObject admin() {
        JSONObject jO = new JSONObject();
        jO.put("result", "/admin");
        return jO;
    }

    @RequestMapping(value = "/errorFunction")
    public JSONObject errorFunction() {
        JSONObject jO = new JSONObject();
        Integer.parseInt("zxc");
        jO.put("result", "/admin");
        return jO;
    }

    @RequestMapping(value = "/provider")
    public JSONObject provider() {
        JSONObject jO = new JSONObject();
        jO.put("result", "/provider");
        return jO;
    }

    @RequestMapping(value = "/user")
    public JSONObject user() {
        JSONObject jO = new JSONObject();
        jO.put("result", "/user");
        return jO;
    }

    @RequestMapping(value = "/about")
    public JSONObject about() {
        JSONObject jO = new JSONObject();
        jO.put("result", "/about");
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

    @RolesAllowed({"ADMIN", "PROVIDER"})
    @RequestMapping(value = "/auth1")
    public JSONObject auth1() {
        JSONObject jO = new JSONObject();
        jO.put("result", "/auth1");
        jO.put("result-getAuthentication", SecurityContextHolder.getContext().getAuthentication());
        return jO;
    }

    @RolesAllowed({"ADMIN1", "PROVIDER1"})
    @RequestMapping(value = "/auth2")
    public JSONObject auth2() {
        JSONObject jO = new JSONObject();
        jO.put("result", "/auth2");
        jO.put("result-getAuthentication", SecurityContextHolder.getContext().getAuthentication());
        return jO;
    }

    @PermitAll
    @RequestMapping(value = "/all")
    public JSONObject all() {
        JSONObject jO = new JSONObject();
        jO.put("result", "everyone can login !");
        return jO;
    }

    @PermitAll
    @RequestMapping(value = "/error1")
    public JSONObject error() {
        JSONObject jO = new JSONObject();
        jO.put("result", "error page");
        return jO;
    }

    @RequestMapping(value = "/logout1")
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
