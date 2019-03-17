package com.github.missthee.db.primary.service;


import com.alibaba.fastjson.JSONObject;
import com.github.missthee.db.primary.entity.SysUser;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SysUserService {
    SysUser selectUserById(String id);

    SysUser selectUserById_username(String id);

    SysUser selectUserByUsername(String loginId);

    SysUser insert(SysUser sysUser);

    JSONObject update(SysUser sysUser);

    boolean delete(String id);

    SysUser test_criteria(String id);

    SysUser test_graph(String id);

    List test_joinFetch(String id);

    Page test_multiCondition(String username, String nickname);


}
