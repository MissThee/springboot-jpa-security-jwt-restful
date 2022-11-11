package com.github.missthee.db.primary.repository;

import com.github.missthee.db.primary.entity.SysPermission;
import com.github.missthee.db.primary.entity.SysRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.QueryHint;
import java.util.Collection;
import java.util.List;

public interface SysPermissionRepository extends JpaRepository<SysPermission, Long> , JpaSpecificationExecutor<SysPermission>{
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    List<SysPermission> findAllByIdIn(Collection<Long> id);
}
