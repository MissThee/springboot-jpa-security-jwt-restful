package com.github.missthee.service.imp;

import com.github.missthee.config.security.jwt.UserInfoForJWT;
import com.github.missthee.config.security.springsecurity.filter.UserInfoForSecurity;
import com.github.missthee.db.entity.*;
import com.github.missthee.db.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthServiceImp implements UserInfoForJWT, UserInfoForSecurity {
    private final SysUserRepository sysUserRepository;
    private final SysUserRoleRepository sysUserRoleRepository;
    private final SysRoleRepository sysRoleRepository;
    private final SysRolePermissionRepository sysRolePermissionRepository;
    private final SysPermissionRepository sysPermissionRepository;

    @Autowired
    public AuthServiceImp(SysUserRepository sysUserRepository, SysUserRoleRepository sysUserRoleRepository, SysRoleRepository sysRoleRepository, SysRolePermissionRepository sysRolePermissionRepository, SysPermissionRepository sysPermissionRepository) {
        this.sysUserRepository = sysUserRepository;
        this.sysUserRoleRepository = sysUserRoleRepository;
        this.sysRoleRepository = sysRoleRepository;
        this.sysRolePermissionRepository = sysRolePermissionRepository;
        this.sysPermissionRepository = sysPermissionRepository;
    }

    //---------------------------权限认证辅助接口实现-------------------------------
    @Override
    public String getSecret(Object obj) {
        SysUser sysUser = sysUserRepository.findById(Long.valueOf(String.valueOf(obj))).orElseThrow(() -> new UsernameNotFoundException("User not found", new Throwable()));
        return sysUser.getPassword();
    }

    private static final String DEFAULT_ROLE_PREFIX = new RoleVoter().getRolePrefix();

    @Override
    public UserDetails loadUserById(Object idObj) {
        Long id = Long.valueOf(String.valueOf(idObj));
        SysUser sysUser = sysUserRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found", new Throwable()));
        {//构造security提供的User类

            //查询user对应的role
            //Example写法
//            List<SysUserRole> sysUserRoleList = sysUserRoleRepository.findAll(
//                    Example.of(
//                            new SysUserRole().setUserId(id),
//                            ExampleMatcher.matching().withMatcher(SysUserRole_.USER_ID, ExampleMatcher.GenericPropertyMatchers.exact()))
//            );
            //Predicate写法
            List<SysUserRole> sysUserRoleList = sysUserRoleRepository.findAll(
                    (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(SysUserRole_.USER_ID), id)
            );
            //获取角色id集合
            Set<Long> roleIdSet = sysUserRoleList.stream().map(SysUserRole::getRoleId).collect(Collectors.toSet());
            //查询role对应的permission
            //Example写法
//            List<SysRolePermission> sysRolePermissionList = sysRolePermissionRepository.findAll(
//                    Example.of(
//                            new SysRolePermission().setRoleId(id),
//                            ExampleMatcher.matching().withMatcher(SysRolePermission_.ROLE_ID, ExampleMatcher.GenericPropertyMatchers.exact())
//                    )
//            );
            //Predicate写法
            List<SysRolePermission> sysRolePermissionList = sysRolePermissionRepository.findAll(
                    (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(SysRolePermission_.roleId), id)
            );
            //获取权限id集合
            Set<Long> permissionIdSet = sysRolePermissionList.stream().map(SysRolePermission::getPermissionId).collect(Collectors.toSet());

            //查询角色对象集合
            List<SysRole> sysRoleList = sysRoleRepository.findAllById(roleIdSet);
            //查询权限对象集合
            List<SysPermission> sysPermissionList = sysPermissionRepository.findAllById(permissionIdSet);

            //收集角色、权限值
            Set<String> authValueList = new HashSet<String>() {{
                //如果前缀是ROLE_，security就会认为这是个角色信息，而不是权限，例如ROLE_MENBER就是MENBER角色，CAN_SEND就是CAN_SEND权限。源码见org.springframework.security.access.vote.RoleVoter
                addAll(sysRoleList.stream().map(e -> DEFAULT_ROLE_PREFIX + e.getRole()).collect(Collectors.toSet()));
                addAll(sysPermissionList.stream().map(SysPermission::getPermission).collect(Collectors.toSet()));
            }};

            //构造security用的SimpleGrantedAuthority集合。（将角色权限值转为security用的权限对象）
            Set<SimpleGrantedAuthority> simpleGrantedAuthoritySet = authValueList
                    .stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toSet());
            //构造security用的用户对象集合
            return new User(String.valueOf(sysUser.getId()), sysUser.getPassword(), simpleGrantedAuthoritySet);//返回包括权限角色的User(此User为security提供的实体类)给security;
        }
    }

}

