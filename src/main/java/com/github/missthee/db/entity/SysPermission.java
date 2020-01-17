package com.github.missthee.db.entity;

import com.github.missthee.db.common.idgenerator.JpaSnowflakeIdGenerator;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
@Accessors(chain = true)
@DynamicUpdate()
@DynamicInsert()
public class SysPermission implements Serializable {
    @Id
//    @GeneratedValue(strategy = GenerationType.TABLE, generator = "PK_GEN")
//    @TableGenerator(pkColumnValue = "SYS_PERMISSION", name = "PK_GEN", table = "TB_GENERATOR", pkColumnName = "GEN_NAME", valueColumnName = "GEN_VALUE", allocationSize = 1)
    @GeneratedValue(generator = JpaSnowflakeIdGenerator.NAME)
    @GenericGenerator(name = JpaSnowflakeIdGenerator.NAME, strategy = JpaSnowflakeIdGenerator.STRATEGY)
    private Long id;//主键.
    private String name;//名称.
    private String permission; //权限字符串,menu例子：role:*，button例子：role:create,role:update,role:delete,role:view
    @ColumnDefault("1")
    private Boolean isEnable;// 是否可用
    @Version
    private Long version;

}