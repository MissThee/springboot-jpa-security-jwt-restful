package com.github.missthee.db.repository;

import com.github.missthee.db.entity.SysPermission;
import com.github.missthee.db.entity.SysRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SysPermissionRepository extends JpaRepository<SysPermission, Long> , JpaSpecificationExecutor<SysPermission>{

}
