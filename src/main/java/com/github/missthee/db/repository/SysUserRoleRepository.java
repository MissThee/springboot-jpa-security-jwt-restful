package com.github.missthee.db.repository;

import com.github.missthee.db.entity.SysUserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SysUserRoleRepository extends JpaRepository<SysUserRole, Long>, JpaSpecificationExecutor<SysUserRole> {
}
