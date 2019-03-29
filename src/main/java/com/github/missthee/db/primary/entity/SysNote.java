package com.github.missthee.db.primary.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table
@Setter
@Getter
@Accessors(chain = true)
public class SysNote implements Serializable {
    @Id
//    @GeneratedValue(strategy = GenerationType.TABLE, generator = "PK_GEN")
//    @TableGenerator(pkColumnValue = "SYS_NOTE", name = "PK_GEN", table = "TB_GENERATOR", pkColumnName = "GEN_NAME", valueColumnName = "GEN_VALUE", allocationSize = 1)
    @GeneratedValue(generator = "JpaSnowflakeIdGenerator")
    @GenericGenerator(name = "JpaSnowflakeIdGenerator", strategy = "com.github.missthee.db.common.idgenerator.JpaSnowflakeIdGenerator")
    private String id;//主键.
    private String note;
}