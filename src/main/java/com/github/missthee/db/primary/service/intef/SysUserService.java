package com.github.missthee.db.primary.service.intef;


import com.alibaba.fastjson.JSONObject;
import com.github.missthee.db.primary.entity.SysUser;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface SysUserService {
    SysUser selectUserById(String id);

    SysUser findFirstByUsernameQuery(String username);

    SysUser selectUserByUsername(String loginId);

    SysUser insert(SysUser sysUser);

    SysUser update(SysUser sysUser);

    void delete(String id);

    List<SysUser> criteria(String id);

    SysUser graph(String id);

    List joinFetchQuery(String id);

    Page multiConditionSearch(Map<String, Object> searchMap);


}
