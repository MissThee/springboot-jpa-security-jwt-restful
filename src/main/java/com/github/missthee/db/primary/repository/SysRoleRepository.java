package com.github.missthee.db.primary.repository;

import com.github.missthee.db.primary.entity.SysRole;
import com.github.missthee.db.primary.entity.SysRolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.QueryHint;
import java.util.Collection;
import java.util.List;

public interface SysRoleRepository extends JpaRepository<SysRole, Long> , JpaSpecificationExecutor<SysRole>{
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    List<SysRole> findAllByIdIn(Collection<Long> id);
}
