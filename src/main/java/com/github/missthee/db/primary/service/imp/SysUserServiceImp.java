package com.github.missthee.db.primary.service.imp;

import com.github.missthee.db.primary.entity.SysPermission;
import com.github.missthee.db.primary.entity.SysRole;
import com.github.missthee.db.primary.entity.SysUser;
import com.github.missthee.db.primary.entity.SysUser_;
import com.github.missthee.db.primary.repository.UserRepository;
import com.github.missthee.db.primary.service.intef.SysUserService;

import com.github.missthee.security.jwt.UserInfoForJWT;
import com.github.missthee.security.security.filter.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import java.util.*;

@Service
public class SysUserServiceImp implements SysUserService, UserInfoForJWT, UserInfo {
    private final UserRepository userRepository;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    public SysUserServiceImp(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public SysUser selectUserById(String id) {
        return userRepository.findFirstById(id);
    }

    @Override
    public SysUser findFirstByUsernameQuery(String username) {
        return userRepository.findFirstByUsernameQuery(username);
    }

    @Override
    public SysUser selectUserByUsername(String username) {
//        SysUser sysUser = em.createQuery(
//                "select l " +
//                        "from SysUser l " +
//                        "inner join fetch l.roleList b " +
//                        "inner join fetch b.permissionList t " +
//                        "where l.username = :username", SysUser.class)
//                .setParameter("username", username)
//                .getSingleResult();
        //使用join fetch 方法，可指定关联表进行eager加载,解决n+1问题
        return userRepository.findFirstByUsernameQuery(username);
    }

    @Override
    public SysUser insert(SysUser sysUser) {
        String encodedPassword = new BCryptPasswordEncoder().encode(sysUser.getPassword());
        sysUser.setPassword(encodedPassword);
        return userRepository.saveAndFlush(sysUser);
    }

    @Override
    public SysUser update(SysUser sysUser) {
        sysUser.setPassword(new BCryptPasswordEncoder().encode(sysUser.getPassword()));
        return userRepository.saveAndFlush(sysUser);
    }

    @Override
    public void delete(String id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<SysUser> criteria(String id) {
        CriteriaBuilder cb = em.getCriteriaBuilder();//安全查询创建工厂(各种比较、运算逻辑或函数的生产)
        CriteriaQuery<SysUser> cq = cb.createQuery(SysUser.class);//安全查询主语句（sql关键词：select,where,group by等，及内容拼接）
        Root<SysUser> root = cq.from(SysUser.class);//它与SQL查询中的FROM子句类似。
        cq.where(cb.equal(root.get(SysUser_.id), id));
        return em.createQuery(cq).getResultList();
    }

    @Override
    public SysUser graph(String id) {
        EntityGraph graph = this.em.createEntityGraph("SysUser.g1");
        Map<String, Object> props = new HashMap<String, Object>() {{
//            put(org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.FETCH.getKey(), graph);
//            put(org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.LOAD.getKey(), graph);
//props.put("javax.persistence.loadgraph", graph);
//props.put("javax.persistence.fetchgraph", graph);
//loadgraph：在原有Entity的定义的基础上，定义还需要获取什么字段/关系
//fetchgraph：完全放弃原有Entity的定义，定义仅需要获取什么字段/关系
//注意上面的”还”和”仅”，表达了两者最大的不同点。
//举个例子，如果我们的Department类型中还有一个name字段：
//loadgraph：被加载的数据为name以及employees
//fetchgraph：被加载的数据仅为employees
        }};
        return em.find(SysUser.class, id, props);
    }

    @Override
    public List joinFetchQuery(String id) {
        Query query = em.createQuery(
                "select " +
                        "new map(u.username as username,r.role as role,p.permission as permission) " +
                        "from SysUser u " +
                        "left join  u.roleList r " +
                        "left join  r.permissionList p " +
                        "where u.id = :id")
                .setParameter("id", id);
        return query.getResultList();
    }

    @Override
    public Page multiConditionSearch(Map<String, Object> searchMap) {
        //条件个数不定，单表查询
        Specification<SysUser> specification = (root, criteriaQuery, criteriaBuilder) -> {
            {
                List<Predicate> predicates = new ArrayList<>();
                for (String key : searchMap.keySet()) {
                    predicates.add(criteriaBuilder.like(root.get(key), "%" + searchMap.get(key) + "%"));
                }
                criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
                return null;
            }
        };
        PageRequest pageRequest = PageRequest.of(0, 50, new Sort(Sort.Direction.DESC, "id"));
        return userRepository.findAll(specification, pageRequest);
    }


    @Override
    public String getSecret(Object obj) {
        SysUser sysUser = userRepository.findFirstById(obj.toString());
        return sysUser.getPassword();
    }

    @Override
    public UserDetails loadUserById(String id) throws UsernameNotFoundException {
        SysUser sysUser = userRepository.findFirstById(id);
        return transToUserDetails(sysUser);
    }

    private UserDetails transToUserDetails(SysUser sysUser) {
        if (sysUser == null) {
            throw new UsernameNotFoundException("User not found", new Throwable());
        }
        List<String> authList = new ArrayList<>(); //GrantedAuthority是security提供的权限类，
        Set<SysRole> roleList = sysUser.getRoleList();
        for (SysRole role : roleList) {
            authList.add("ROLE_" + role.getRole());
            for (SysPermission permission : role.getPermissionList()) {
                authList.add(permission.getPermission());
            }
        }
        //权限如果前缀是ROLE_，security就会认为这是个角色信息，而不是权限，例如ROLE_MENBER就是MENBER角色，CAN_SEND就是CAN_SEND权限

        List<SimpleGrantedAuthority> list = new ArrayList<>();
        for (String auth : authList) {
            list.add(new SimpleGrantedAuthority(auth));
        }
        return new User(sysUser.getId(), sysUser.getPassword(), list);//返回包括权限角色的User(此User为security提供的实体类)给security;
    }
}

