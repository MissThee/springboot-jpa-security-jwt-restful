package com.github.missthee.db.primary.entity;


import com.github.missthee.db.common.idgenerator.JpaSnowflakeIdGenerator;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.Hibernate;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;


@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Accessors(chain = true)
@DynamicUpdate()//更新时update语句只构建变动的字段（即数据库中查出的数据与当前实体类中差异的部分）
@DynamicInsert()//插入时insert语句只构建变动的字段（即数据库中查出的数据与当前实体类中差异的部分，因插入时数据库一般查不到数据，所有字段认为是null。）
@Cacheable
public class SysUser implements Serializable {
    @Id
//   @GeneratedValue的 AUTO　自动选择一个最适合底层数据库的主键生成策略。如MySQL会自动对应auto increment。这个是默认选项，即如果只写@GeneratedValue，等价于@GeneratedValue(strategy=GenerationType.AUTO)
//    @GeneratedValue(strategy = GenerationType.TABLE, generator = "PK_GEN")
//    @TableGenerator(pkColumnValue = "SYS_USER", name = "PK_GEN", table = "TB_GENERATOR", pkColumnName = "GEN_NAME", valueColumnName = "GEN_VALUE", allocationSize = 1)
    @GeneratedValue(generator = JpaSnowflakeIdGenerator.NAME)
    @GenericGenerator(name = JpaSnowflakeIdGenerator.NAME, strategy = JpaSnowflakeIdGenerator.STRATEGY)
    private Long id;
    @Column(unique = true)//唯一值约束
    private String username; //帐号
    private String nickname; //昵称
    private String password; //密码;
    //@Column(columnDefinition = "int default 1") //定义默认值
    @ColumnDefault("1")
    private Boolean isEnable;      //用户状态
    @Version
    @ColumnDefault("0")
    private Long version;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        SysUser sysUser = (SysUser) o;
        return id != null && Objects.equals(id, sysUser.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}



