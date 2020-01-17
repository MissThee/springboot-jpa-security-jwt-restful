package com.github.missthee.db.entity;

import com.github.missthee.db.common.idgenerator.JpaSnowflakeIdGenerator;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Data
@Accessors(chain = true)
@DynamicUpdate()
@DynamicInsert()
public class SysRole {
    @Id
//    @GeneratedValue(strategy = GenerationType.TABLE, generator = "PK_GEN")
//    @TableGenerator(pkColumnValue = "SYS_ROLE", name = "PK_GEN", table = "TB_GENERATOR", pkColumnName = "GEN_NAME", valueColumnName = "GEN_VALUE", allocationSize = 1)
    @GeneratedValue(generator = JpaSnowflakeIdGenerator.NAME)
    @GenericGenerator(name = JpaSnowflakeIdGenerator.NAME, strategy = JpaSnowflakeIdGenerator.STRATEGY)
    private Long id; // 编号
    private String name; // 角色名
    private String role; // 角色标识程序中判断使用,如"admin",这个是唯一的:
    @ColumnDefault("1")
    private Boolean isEnable; // 是否可用
    @Version
    private Long version;
}