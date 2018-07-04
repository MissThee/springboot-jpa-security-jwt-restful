package com.tenmax.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoginController {
    @RequestMapping(value = "/login1")
    public JSONObject Login(@RequestBody(required = false) JSONObject dataJO) {
        JSONObject jO = new JSONObject();
        jO.put("result", "true");
        jO.put("data", dataJO);
        return jO;
    }
}
