package com.github.missthee.db.primary.repository;

import com.github.missthee.db.primary.entity.SysPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SysPermissionRepository extends JpaRepository<SysPermission, Long> , JpaSpecificationExecutor<SysPermission>{

}
