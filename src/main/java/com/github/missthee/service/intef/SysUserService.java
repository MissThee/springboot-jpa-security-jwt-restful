package com.github.missthee.service.intef;


import com.github.missthee.db.primary.entity.SysUser;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface SysUserService {
    SysUser insert(SysUser sysUser);

    SysUser update(SysUser sysUser);

    void delete(Long id);

    SysUser selectUserById(Long id);

    SysUser selectUserByUsername(String username);

    Page<SysUser> multiConditionSearch(Map<String, String> searchMap,Integer pageNum,Integer pageSize);

    List<SysUser> emCriteria(Long id);

    SysUser emGraph(Long id);

    List<?> emJoinFetchQuery(Long id);


}
