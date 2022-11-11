package com.github.missthee.db.primary.repository;

import com.github.missthee.db.primary.entity.SysRolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.Cacheable;
import javax.persistence.QueryHint;
import java.util.Collection;
import java.util.List;


public interface SysRolePermissionRepository extends JpaRepository<SysRolePermission, Long>, JpaSpecificationExecutor<SysRolePermission> {
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    List<SysRolePermission> findAllByRoleIdIn(Collection<Long> id);
}
