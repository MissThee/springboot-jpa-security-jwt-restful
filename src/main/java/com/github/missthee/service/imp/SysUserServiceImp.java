package com.github.missthee.service.imp;

import com.github.missthee.db.entity.SysPermission;
import com.github.missthee.db.entity.SysRole;
import com.github.missthee.db.entity.SysUser;
import com.github.missthee.db.entity.SysUser_;
import com.github.missthee.db.repository.UserRepository;
import com.github.missthee.service.intef.SysUserService;

import com.github.missthee.config.security.jwt.UserInfoForJWT;
import com.github.missthee.config.security.security.filter.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional(rollbackFor = Exception.class)
    public SysUser insert(SysUser sysUser) {
        Optional<SysUser> sysUserOp = userRepository.findFirstByUsername(sysUser.getUsername());
        if (sysUserOp.isPresent()) {
            return null;
        }
        String encodedPassword = new BCryptPasswordEncoder().encode(sysUser.getPassword());
        sysUser.setPassword(encodedPassword);
        return userRepository.saveAndFlush(sysUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysUser update(SysUser sysUser) {
        Optional<SysUser> sysUserOp = userRepository.findFirstByUsername(sysUser.getUsername());
        if (sysUserOp.isPresent()) {
            if (!sysUserOp.get().getId().equals(sysUser.getId())) {
                return null;
            }
        }
        sysUser.setPassword(new BCryptPasswordEncoder().encode(sysUser.getPassword()));
        return userRepository.saveAndFlush(sysUser);
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public SysUser selectUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found", new Throwable()));
    }

    @Override
    public SysUser selectUserByUsername(String username) {
        //hql语句使用join fetch 方法，可指定关联表进行eager加载,解决n+1问题
//        SysUser sysUser = em.createQuery(
//                "select l " +
//                        "from SysUser l " +
//                        "inner join fetch l.roleList b " +
//                        "inner join fetch b.permissionList t " +
//                        "where l.username = :username", SysUser.class)
//                .setParameter("username", username)
//                .getSingleResult();
        return userRepository.findFirstByUsernameQuery(username);
    }

    @Override
    public Page<SysUser> multiConditionSearch(Map<String, String> searchMap, Integer pageNum, Integer pageSize) {
        //条件个数不定，单表查询
        Specification<SysUser> specification =
                searchMap == null ? null : (root, criteriaQuery, criteriaBuilder) -> {
                    Predicate predicate = criteriaBuilder.and(
                            searchMap.keySet().stream()
                                    .map(key -> criteriaBuilder.like(root.get(key), "%" + searchMap.get(key) + "%"))
                                    .toArray(Predicate[]::new)
                    );
                    criteriaQuery.where(predicate);
                    return predicate;
                };
        Pageable pageable = (pageNum == null || pageSize == null) ? Pageable.unpaged() : PageRequest.of(pageNum - 1, pageSize, Sort.Direction.ASC, "id");
        return userRepository.findAll(specification, pageable);
    }

    @Override
    public List<SysUser> emCriteria(Long id) {
        CriteriaBuilder cb = em.getCriteriaBuilder();//安全查询创建工厂(各种比较、运算逻辑或函数的生产)
        CriteriaQuery<SysUser> cq = cb.createQuery(SysUser.class);//安全查询主语句（sql关键词：select,where,group by等，及内容拼接）
        Root<SysUser> root = cq.from(SysUser.class);//它与SQL查询中的FROM子句类似。
        cq.where(cb.equal(root.get(SysUser_.id), id));
        return em.createQuery(cq).getResultList();
    }

    @Override
    public SysUser emGraph(Long id) {
        EntityGraph graph = this.em.createEntityGraph(SysUser.NamedEntityGraph.Graph1);
        Map<String, Object> props = new HashMap<String, Object>() {{
//            put(org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.FETCH.getKey(), graph);
            //put(org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.LOAD.getKey(), graph);
            //loadgraph：在原有Entity的定义的基础上，额外需要获取什么字段/关系
            //fetchgraph：完全放弃原有Entity的定义，定义需要获取什么字段/关系
        }};
        return em.find(SysUser.class, id, props);
    }

    @Override
    public List<?> emJoinFetchQuery(Long id) {
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


    //---------------------------权限认证辅助接口实现-------------------------------
    @Override
    public String getSecret(Object obj) {
        SysUser sysUser = userRepository.findById(Long.valueOf(String.valueOf(obj))).orElseThrow(() -> new UsernameNotFoundException("User not found", new Throwable()));
        return sysUser.getPassword();
    }

    @Override
    public UserDetails loadUserById(String id) {
        SysUser sysUser = userRepository.findById(Long.valueOf(id)).orElseThrow(() -> new UsernameNotFoundException("User not found", new Throwable()));
        return transToUserDetails(sysUser);
    }

    private UserDetails transToUserDetails(SysUser sysUser) {
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
        return new User(String.valueOf(sysUser.getId()), sysUser.getPassword(), list);//返回包括权限角色的User(此User为security提供的实体类)给security;
    }
}

