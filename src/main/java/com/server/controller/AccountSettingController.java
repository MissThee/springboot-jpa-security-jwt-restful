package com.tenmax.controller;

import com.alibaba.fastjson.JSONObject;
import com.tenmax.db.primary.entity.SysUser;
import com.tenmax.db.primary.service.SysUserService;
import com.tenmax.db.primary.tool.BeanTool;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(value = "/user")
@RestController
public class AccountSettingController {

    @Autowired
    SysUserService sysUserService;

    @PutMapping()
    @ApiOperation(value = "新增用户", notes = "提交用户信息，新增用户")
    @ApiImplicitParam(name = "sysUser", value = "用户实体", required = true, dataType = "SysUser", paramType = "application/json")
    public Object signUp(@RequestBody SysUser sysUser) {
        return sysUserService.insert(sysUser) != null;
    }

    @PatchMapping()
    @ApiOperation(value = "修改用户", notes = "提交用户信息，修改用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sysUser", value = "用户实体user", required = true, dataType = "SysUser")
    })
    public Object changeInfo(@RequestBody SysUser sysUser) {
        SysUser user = sysUserService.selectUserById(sysUser.getId());
        BeanTool.copyPropertiesWithoutNull(sysUser, user);
        return sysUserService.update(user);
    }

    @DeleteMapping()
    @ApiOperation(value = "删除用户", notes = "提交用户信息，修改用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户id", required = true, dataType = "int")
    })
    public Object deleteInfo(@RequestBody int id) {
        JSONObject jO = new JSONObject();
        jO.put("result", sysUserService.delete(id));
        return jO;
    }

    @GetMapping(value = "/getInfo/{id}")
    @ApiOperation(value = "查找用户byId", notes = "提交id，获取用户")
    @ApiImplicitParam(name = "id", value = "用户id", required = true, dataType = "int")
    public Object getInfo(@PathVariable("id") int id) {
        JSONObject jO = new JSONObject();
        SysUser sysUser = sysUserService.selectUserById(id);
        jO.put("result", sysUser);
        System.out.println(jO.toString());
        return jO;
    }

    @GetMapping(value = "/test/{id}")
    public JSONObject test(@PathVariable("id") int id) {
        JSONObject jO = new JSONObject();
        jO.put("result", sysUserService.test_criteria(id));
        return jO;
    }

    @GetMapping(value = "/test_graph/{id}")
    public JSONObject test_graph(@PathVariable("id") int id) {
        JSONObject jO = new JSONObject();
        jO.put("result", sysUserService.test_graph(id));
        return jO;
    }

    @GetMapping(value = "/test_joinFetch/{id}")
    public List test_joinFetch(@PathVariable("id") int id) {
        return sysUserService.test_joinFetch(id);
    }

    @GetMapping(value = "/selectUserById_username/{id}")
    public JSONObject selectUserById_username(@PathVariable("id") int id) {
        JSONObject jO = new JSONObject();
        SysUser sysUser = sysUserService.selectUserById_username(id);
        sysUser.setRoleList(null);
        jO.put("result", sysUser);
        return jO;
    }

    @GetMapping(value = "/test_multiCondition")
    public JSONObject test_multiCondition(@RequestParam(value = "username", required = false) String username, @RequestParam(value = "nickname", required = false) String nickname) {
        JSONObject jO = new JSONObject();
        Page page = sysUserService.test_multiCondition(username, nickname);
        jO.put("result", page);
        return jO;
    }
}

