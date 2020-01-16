package com.github.missthee.db.repository;

import com.github.missthee.db.entity.SysUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;

import java.util.Optional;

public interface UserRepository extends JpaRepository<SysUser, Long>, JpaSpecificationExecutor<SysUser> {

    @Query(value = "select t from SysUser t where t.username=:username")
    SysUser findFirstByUsernameQuery(@Param("username") String username);

    Optional<SysUser> findFirstByUsername(String username);

    @EntityGraph(value = SysUser.NamedEntityGraph.Graph1, type = EntityGraph.EntityGraphType.FETCH)
    Page<SysUser> findAll(Specification<SysUser> specification, Pageable pageable);

}
