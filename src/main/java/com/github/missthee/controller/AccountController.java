package com.github.missthee.controller;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonView;
import com.github.missthee.db.primary.entity.SysUser;
import com.github.missthee.db.primary.service.intef.SysUserService;
import com.github.missthee.db.primary.tool.BeanTool;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    public Object deleteInfo(@RequestBody String id) {
        JSONObject jO = new JSONObject();
        jO.put("result", sysUserService.delete(id));
        return jO;
    }

    @GetMapping(value = "/getInfo/{id}")
    @ApiOperation(value = "查找用户byId", notes = "提交id，获取用户")
    @ApiImplicitParam(name = "id", value = "用户id", required = true, dataType = "int")
    public Object getInfo(@PathVariable("id") String id) {
        JSONObject jO = new JSONObject();
        SysUser sysUser = sysUserService.selectUserById(id);
        jO.put("result", sysUser);
        System.out.println(jO.toString());
        return jO;
    }

    @GetMapping(value = "/criteria/{id}")
    public JSONObject criteria(@PathVariable("id") String id) {
        JSONObject jO = new JSONObject();
        jO.put("result", sysUserService.criteria(id));
        return jO;
    }

    @GetMapping(value = "/criteria1/{id}")
    public JSONObject criteria1(@PathVariable("id") String id) {
        JSONObject jO = new JSONObject();
        jO.put("result", sysUserService.criteria1(id));
        return jO;
    }

    @GetMapping(value = "/graph/{id}")
    @JsonView(SysUser.UserPasswordView.class)
    public JSONObject test_graph(@PathVariable("id") String id) {
        JSONObject jO = new JSONObject();
        jO.put("result", sysUserService.graph(id));
        return jO;
    }

    @GetMapping(value = "/joinFetch/{id}")
    public List joinFetch(@PathVariable("id") String id) {
        return sysUserService.joinFetch(id);
    }

    @GetMapping(value = "/selectUserById_username/{id}")
    public JSONObject selectUserById_username(@PathVariable("id") String id) {
        JSONObject jO = new JSONObject();
        SysUser sysUser = sysUserService.selectUserById_username(id);
        sysUser.setRoleList(null);
        jO.put("result", sysUser);
        return jO;
    }

    @PostMapping(value = "/multiConditionSearch")
    public JSONObject multiConditionSearch(@RequestBody JSONObject bJO) {
        JSONObject jO = new JSONObject();
        Map<String, Object> searchMap = bJO.getJSONObject("searchMap");
        Page page = sysUserService.multiConditionSearch(searchMap);
        jO.put("result", page);
        return jO;
    }
}

