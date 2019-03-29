package com.github.missthee.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.github.missthee.db.primary.entity.SysUser;
import com.github.missthee.db.primary.service.intef.SysUserService;
import com.github.missthee.tool.Res;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping(value = "/user")
@RestController
public class AccountController {

    private final SysUserService sysUserService;
    private final MapperFacade mapperFacade;

    @Autowired
    public AccountController(SysUserService sysUserService, MapperFacade mapperFacade) {
        this.sysUserService = sysUserService;
        this.mapperFacade = mapperFacade;
    }

    @PutMapping()
    @ApiOperation(value = "新增用户", notes = "提交用户信息，新增用户")
    @ApiImplicitParam(name = "sysUser", value = "用户实体", required = true, dataType = "SysUser", paramType = "application/json")
    public Res insertUser(@RequestBody SysUser sysUser) {
        return Res.res(sysUserService.insert(sysUser) != null);
    }

    @PatchMapping()
    @ApiOperation(value = "修改用户", notes = "提交用户信息，修改用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sysUser", value = "用户实体user", required = true, dataType = "SysUser")
    })
    public Res updateUser(@RequestBody SysUser sysUser) {
        SysUser user = sysUserService.selectUserById(sysUser.getId());
        mapperFacade.map(sysUser, user);
        return Res.success(sysUserService.update(user));
    }

    @DeleteMapping()
    @ApiOperation(value = "删除用户", notes = "提交用户信息，修改用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户id", required = true, dataType = "int")
    })
    public Res deleteUser(@RequestBody String id) {
        sysUserService.delete(id);
        return Res.success(true);
    }

    @GetMapping(value = "{id}")
    @ApiOperation(value = "查找用户byId", notes = "提交id，获取用户")
    @ApiImplicitParam(name = "id", value = "用户id", required = true, dataType = "int")
    public Res<SysUser> selectUserById(@PathVariable("id") String id) {
        return Res.success(sysUserService.selectUserById(id));
    }

    @GetMapping(value = "/criteria/{id}")
    public Res<List<SysUser>> criteria(@PathVariable("id") String id) {
        return Res.success(sysUserService.criteria(id));
    }

    @GetMapping(value = "/graph/{id}")
    @JsonView(SysUser.UserPasswordView.class)
    public Res<SysUser> graph(@PathVariable("id") String id) {
        return Res.success(sysUserService.graph(id));
//        使用动态语句构建类构建的查询。
//        实际查询语句为普通嵌套查询，不适合生产环境使用。如查询user列表，查询每个user的role列表，再查询每个role的permission列表。
    }

    @GetMapping(value = "/joinFetch/{id}")
    public Res<List> joinFetch(@PathVariable("id") String id) {
        return Res.success(sysUserService.joinFetchQuery(id));
//        使用hql语句构建的查询。(对象构建)
//        实际查询语句为left join直接关联查询，返回Map集合，可自由组合多表的字段
    }

    @GetMapping(value = "/findFirstByUsernameQuery/{username}")
    public Res<SysUser> findFirstByUsernameQuery(@PathVariable("username") String username) {
        return Res.success(sysUserService.findFirstByUsernameQuery(username));
//        使用hql语句构建的查询。(注解构建)
    }

    @PostMapping(value = "/multiConditionSearch")
    public Res multiConditionSearch(@RequestBody Map<String, Object> bJO) {
        Map<String, Object> searchMap = (Map<String, Object>) bJO.get("searchMap");
        searchMap = searchMap == null ? new HashMap<>() : searchMap;

        Page page = sysUserService.multiConditionSearch(searchMap);
        return Res.success(new HashMap<String, Object>() {{
            put("content", page.getContent());
            put("total", page.getTotalElements());
        }});
    }
}

