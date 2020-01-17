package com.github.missthee.db.entity;

import com.github.missthee.db.common.idgenerator.JpaSnowflakeIdGenerator;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Version;

@Entity
@Data
@Accessors(chain = true)
@DynamicUpdate()
@DynamicInsert()
public class SysRolePermission {
    @Id
//    @GeneratedValue(strategy = GenerationType.TABLE, generator = "PK_GEN")
//    @TableGenerator(pkColumnValue = "SYS_ROLE_PERMISSION", name = "PK_GEN", table = "TB_GENERATOR", pkColumnName = "GEN_NAME", valueColumnName = "GEN_VALUE", allocationSize = 1)
    @GeneratedValue(generator = JpaSnowflakeIdGenerator.NAME)
    @GenericGenerator(name = JpaSnowflakeIdGenerator.NAME, strategy = JpaSnowflakeIdGenerator.STRATEGY)
    private Long id;
    private Long roleId;
    private Long permissionId;
    @Version
    private Long version;
}