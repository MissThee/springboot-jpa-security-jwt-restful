package com.github.missthee.db.primary.repository;

import com.github.missthee.db.primary.entity.SysRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SysRoleRepository extends JpaRepository<SysRole, Long> , JpaSpecificationExecutor<SysRole>{

}
