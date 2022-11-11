package com.github.missthee.db.primary.repository;

import com.github.missthee.db.primary.entity.SysRole;
import com.github.missthee.db.primary.entity.SysUserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.QueryHint;
import java.util.Collection;
import java.util.List;

public interface SysUserRoleRepository extends JpaRepository<SysUserRole, Long>, JpaSpecificationExecutor<SysUserRole> {
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    List<SysUserRole> findAllByUserId(Long id);
}
