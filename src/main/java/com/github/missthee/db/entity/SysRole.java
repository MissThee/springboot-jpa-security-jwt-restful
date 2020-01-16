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
@Table
@Setter
@Getter
@Accessors(chain = true)
@NamedEntityGraphs({
        @NamedEntityGraph(
                name = SysRole.NamedEntityGraph.Graph1,
                attributeNodes = {@NamedAttributeNode(value = SysRole_.PERMISSION_LIST, subgraph = SysPermission.NamedEntityGraph.Graph1)})
})
@DynamicUpdate()
@DynamicInsert()
public class SysRole {
    public interface NamedEntityGraph {
        String Graph1 = "SysRole.Graph1";
    }

    @Id
//    @GeneratedValue(strategy = GenerationType.TABLE, generator = "PK_GEN")
//    @TableGenerator(pkColumnValue = "SYS_ROLE", name = "PK_GEN", table = "TB_GENERATOR", pkColumnName = "GEN_NAME", valueColumnName = "GEN_VALUE", allocationSize = 1)
    @GeneratedValue(generator = JpaSnowflakeIdGenerator.NAME)
    @GenericGenerator(name = JpaSnowflakeIdGenerator.NAME, strategy = JpaSnowflakeIdGenerator.STRATEGY)
    private Long id; // 编号
    private String role; // 角色标识程序中判断使用,如"admin",这个是唯一的:
    private String name; // 角色名
    @Column(columnDefinition = "int default 1") //定义默认值
    private Boolean isEnable; // 是否可用

    // 角色 - 权限关系;
    @ManyToMany(fetch = FetchType.LAZY)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinTable(
            name = "sys_role_permission",
            joinColumns = {@JoinColumn(name = "role_id", foreignKey = @ForeignKey(name = "none"))},
            inverseJoinColumns = {@JoinColumn(name = "permission_id", foreignKey = @ForeignKey(name = "none"))})
    private Set<SysPermission> permissionList;
//开启以下映射关系，需要将@Data换为@Setter @Getter。使用@Data，其中@ToString会将双向多对多字段内容死循环遍历输出，导致堆栈溢出错误
//    // 角色 - 用户关系;
//    @ManyToMany
//    @JoinTable(name = "sys_user_role", joinColumns = {@JoinColumn(name = "role_id")}, inverseJoinColumns = {@JoinColumn(name = "user_id")}, foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
//    private Set<SysUser> userList;// 一个角色对应多个用户

}