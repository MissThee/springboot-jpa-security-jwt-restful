package com.server.db.primary.service.imp;

import com.alibaba.fastjson.JSONObject;
import com.server.db.primary.entity.SysUser;
import com.server.db.primary.entity.SysUser_;
import com.server.db.primary.repository.UserRepository;
import com.server.db.primary.service.SysUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import javax.persistence.criteria.*;
import java.util.*;

@Service
public class SysUserServiceImp implements SysUserService {
    @Autowired
    private UserRepository userRepository;

    @PersistenceContext
    private EntityManager em;

    @Override
    public SysUser selectUserById(int id) {
        SysUser sysUser = userRepository.findFirstById(id);
        return sysUser;
    }

    @Override
    public SysUser selectUserById_username(int id) {
        SysUser sysUser = userRepository.findFirstByUsernameQuery("a");
        return sysUser;
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
        SysUser sysUser = userRepository.findFirstByUsernameQuery(username);
        return sysUser;
    }

    @Override
    public SysUser insert(SysUser sysUser) {
        sysUser.setId(null);
        String encodedPassword = new BCryptPasswordEncoder().encode(sysUser.getPassword());
        sysUser.setPassword(encodedPassword);
        return userRepository.saveAndFlush(sysUser);
    }

    @Override
    public JSONObject update(SysUser sysUser) {
        JSONObject jO = new JSONObject();
        String encodedPassword = new BCryptPasswordEncoder().encode(sysUser.getPassword());
        sysUser.setPassword(encodedPassword);
        boolean result = false;
        String msg = "";
        try {
            result = true;
            sysUser = userRepository.saveAndFlush(sysUser);
        } catch (Exception e) {
            e.getMessage();
            result = false;
            sysUser = null;
            msg=e.toString();
        }
        jO.put("result", result);
        jO.put("data", sysUser);
        jO.put("msg",msg);
        return jO;
    }

    @Override
    public boolean delete(int id) {
        boolean result;
        try {
            result = true;
            userRepository.deleteById(id);
        } catch (Exception e) {
            result = false;
            e.printStackTrace();
        }
        return result;
    }


    @Override
    public SysUser test_criteria(int id) {
        CriteriaBuilder cb = em.getCriteriaBuilder();//安全查询创建工厂
        CriteriaQuery cq = cb.createQuery(SysUser.class);//安全查询主语句
        Root sysUserRoot = cq.from(SysUser.class);//它与SQL查询中的FROM子句类似。
        sysUserRoot.fetch(SysUser_.roleList, JoinType.INNER);
        cq.select(sysUserRoot);
        Predicate condition = cb.equal(sysUserRoot.get(SysUser_.id), id);//过滤条件
        cq.where(condition);
        SysUser sysUser = (SysUser) em.createQuery(cq).getSingleResult();
        return sysUser;
    }

    @Override
    public SysUser test_graph(int id) {
//        EntityGraph graph = this.em.createEntityGraph("SysUser.g1");
//        Map<String, Object> props = new HashMap<>();
//        props.put("javax.persistence.fetchgraph", graph);
//        SysUser sysUser = em.find(SysUser.class, id, props);
        SysUser sysUser = em.find(SysUser.class, id);
        return sysUser;
    }

    @Override
    public List test_joinFetch(int id) {
        Query query = em.createQuery(
                "select " +
                        "new map(u.username as username,r.role as role,p.permission as permission) " +
                        "from SysUser u " +
                        "left join  u.roleList r " +
                        "left join  r.permissionList p " +
                        "where u.id = :Id")
                .setParameter("Id", id);
        return query.getResultList();
    }

    @Override
    public Page test_multiCondition(String username, String nickname) {
        //条件个数不定，单表查询
        Specification<SysUser> specification = (root, criteriaQuery, criteriaBuilder) -> {
            {
                List<Predicate> predicates = new ArrayList<>();
                if (!StringUtils.isEmpty(nickname)) {
                    predicates.add(criteriaBuilder.like(root.get(SysUser_.NICKNAME), "%" + nickname + "%"));
                }
                if (!StringUtils.isEmpty(username)) {
                    predicates.add(criteriaBuilder.like(root.get(SysUser_.USERNAME), "%" + username + "%"));
                }
                criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()])));
                return null;
            }
        };
        PageRequest pageRequest = PageRequest.of(0, 50, new Sort(Sort.Direction.DESC, "id"));
        Page page = userRepository.findAll(specification, pageRequest);
        return page;
    }


}

//props.put("javax.persistence.loadgraph", graph);
//props.put("javax.persistence.fetchgraph", graph);
//loadgraph：在原有Entity的定义的基础上，定义还需要获取什么字段/关系
//fetchgraph：完全放弃原有Entity的定义，定义仅需要获取什么字段/关系
//注意上面的”还”和”仅”，表达了两者最大的不同点。
//举个例子，如果我们的Department类型中还有一个name字段：
//loadgraph：被加载的数据为name以及employees
//fetchgraph：被加载的数据仅为employees