package com.github.missthee.db.primary.repository;

import com.github.missthee.db.primary.entity.SysRolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface SysRolePermissionRepository extends JpaRepository<SysRolePermission, Long> , JpaSpecificationExecutor<SysRolePermission>{

}
