package com.tenmax.db.primary.service;


import com.alibaba.fastjson.JSONObject;
import com.tenmax.db.primary.entity.SysUser;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SysUserService {
    SysUser selectUserById(int id);

    SysUser selectUserById_username(int id);

    SysUser selectUserByUsername(String loginId);

    SysUser insert(SysUser sysUser);

    JSONObject update(SysUser sysUser);

    boolean delete(int id);

    SysUser test_criteria(int id);

    SysUser test_graph(int id);

    List test_joinFetch(int id);

    Page test_multiCondition(String username, String nickname);


}
