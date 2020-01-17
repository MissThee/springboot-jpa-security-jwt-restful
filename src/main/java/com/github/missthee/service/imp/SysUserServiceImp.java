package com.github.missthee.service.imp;

import com.github.missthee.db.entity.SysUser;
import com.github.missthee.db.entity.SysUser_;
import com.github.missthee.db.repository.SysUserRepository;
import com.github.missthee.service.intef.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class SysUserServiceImp implements SysUserService {
    private final SysUserRepository userRepository;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    public SysUserServiceImp(SysUserRepository userRepository) {
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
                searchMap == null ? null : (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.and(
                            searchMap.keySet().stream()
                                    .map(key -> criteriaBuilder.like(root.get(key), "%" + searchMap.get(key) + "%"))
                                    .toArray(Predicate[]::new)
                    );
        Pageable pageable = (pageNum == null || pageSize == null) ? Pageable.unpaged() : PageRequest.of(pageNum - 1, pageSize, Sort.Direction.ASC, "id");
        return userRepository.findAll(specification, pageable);
    }

    @Override
    public List<SysUser> emCriteria(Long id) {
        CriteriaBuilder cb = em.getCriteriaBuilder();//安全查询创建工厂，相当于使用prepareStatement可防止sql注入 (各种比较、运算逻辑或函数的生产 and or equal like)
        CriteriaQuery<SysUser> cq = cb.createQuery(SysUser.class);//安全查询主语句 （sql关键词：select,where,group by,order by等语句块引导）
        Root<SysUser> root = cq.from(SysUser.class);//它与SQL查询中的FROM子句类似。
        cq.where(
                cb.or(
                        cb.equal(root.get(SysUser_.id), id),
                        cb.like(root.get(SysUser_.username), "%t%")
                )
        ).orderBy(
                cb.asc(root.get(SysUser_.id))
        );
        return em.createQuery(cq).getResultList();
    }

    @Override
    public SysUser emGraph(Long id) {
        return em.find(SysUser.class, id);
    }

    @Override
    public List<?> emJoinFetchQuery(Long id) {
        //查询结果为 username、role、permission三个字段组成的扁平数据，没有上下级关系
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

}

