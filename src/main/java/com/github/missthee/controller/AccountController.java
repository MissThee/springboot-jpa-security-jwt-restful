package com.github.missthee.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.github.missthee.db.entity.SysUser;
import com.github.missthee.service.intef.SysUserService;
import com.github.missthee.tool.res.Res;
import com.github.missthee.tool.res.SimpleDataList;
import com.github.missthee.vo.user.UserInsertVO;
import com.github.missthee.vo.user.UserSearchVO;
import com.github.missthee.vo.user.UserUpdateVO;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public Res insertUser(@RequestBody UserInsertVO userInsertVO) {
        SysUser sysUser = mapperFacade.map(userInsertVO, SysUser.class);
        return Res.res(sysUserService.insert(sysUser) != null);
    }

    @PatchMapping()
    @ApiOperation(value = "修改用户", notes = "提交用户信息，修改用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sysUser", value = "用户实体user", required = true, dataType = "SysUser")
    })
    public Res updateUser(@RequestBody UserUpdateVO userUpdateVO) {
        SysUser user = sysUserService.selectUserById(userUpdateVO.getId());
        mapperFacade.map(userUpdateVO, user);
        return Res.res(sysUserService.update(user) != null);
    }

    @DeleteMapping()
    @ApiOperation(value = "删除用户", notes = "提交用户信息，修改用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户id", required = true, dataType = "int")
    })
    public Res deleteUser(@RequestBody Long id) {
        sysUserService.delete(id);
        return Res.success();
    }

    @GetMapping(value = "{id}")
    @ApiOperation(value = "查找用户byId", notes = "提交id，获取用户")
    @ApiImplicitParam(name = "id", value = "用户id", required = true, dataType = "int")
    public Res<SysUser> selectUserById(@PathVariable("id") Long id) {
        return Res.success(sysUserService.selectUserById(id));
    }

    @GetMapping(value = "/criteria/{id}")
    public Res<List<SysUser>> criteria(@PathVariable("id") Long id) {
        return Res.success(sysUserService.emCriteria(id));
    }

    @GetMapping(value = "/graph/{id}")
    @JsonView(SysUser.UserPasswordView.class)
    public Res<SysUser> graph(@PathVariable("id") Long id) {
        return Res.success(sysUserService.emGraph(id));
//        使用动态语句构建类构建的查询。
//        实际查询语句为普通嵌套查询，不适合生产环境使用。如查询user列表，查询每个user的role列表，再查询每个role的permission列表。
    }

    @GetMapping(value = "/joinFetch/{id}")
    public Res<?> emJoinFetch(@PathVariable("id") Long id) {
        return Res.success(sysUserService.emJoinFetchQuery(id));
//        使用hql语句构建的查询。(em方法写查询语句构建)
//        实际查询语句为left join直接关联查询，返回Map集合，可自由组合多表的字段
    }

    @GetMapping(value = "/findFirstByUsernameQuery/{username}")
    public Res<SysUser> findFirstByUsernameQuery(@PathVariable("username") String username) {
        return Res.success(sysUserService.selectUserByUsername(username));
//        使用hql语句构建的查询。(注解中写查询语句构建)
    }

    @PostMapping(value = "/multiConditionSearch")
    public Res<SimpleDataList<SysUser>> multiConditionSearch(@RequestBody @Validated UserSearchVO userSearchVO) {
        //动态构建where查询条件
        Page<SysUser> page = sysUserService.multiConditionSearch(
                userSearchVO.getSearchMap(),
                userSearchVO.getPageNum(),
                userSearchVO.getPageSize()
        );
        return Res.success(new SimpleDataList<>(page.getContent(), page.getTotalElements()));
    }
}

