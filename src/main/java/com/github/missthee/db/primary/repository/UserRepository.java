package com.github.missthee.db.primary.repository;

import com.github.missthee.db.primary.entity.SysUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;

public interface UserRepository extends JpaRepository<SysUser, String>, JpaSpecificationExecutor<SysUser> {

    @EntityGraph(value = "SysUser.g1", type = EntityGraph.EntityGraphType.LOAD)
    @Query(value = "select t from SysUser t where t.username=:username")
    SysUser findFirstByUsernameQuery(@Param("username") String username);

    SysUser findFirstById(String id);

    @EntityGraph(value = "SysUser.g1", type = EntityGraph.EntityGraphType.FETCH)
    Page<SysUser> findAll(@Nullable Specification<SysUser> var1, Pageable var2);

}
