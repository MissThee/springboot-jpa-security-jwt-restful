package com.github.missthee.db.repository;

import com.github.missthee.db.entity.SysUser;
import com.github.missthee.db.entity.SysUserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SysUserRoleRepository extends JpaRepository<SysUserRole, Long>, JpaSpecificationExecutor<SysUserRole> {



}
