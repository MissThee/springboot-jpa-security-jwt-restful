package com.github.missthee.db.primary.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table
@Setter
@Getter
@Accessors(chain = true)
public class SysPermission implements Serializable {
    @Id
//    @GeneratedValue(strategy = GenerationType.TABLE, generator = "PK_GEN")
//    @TableGenerator(pkColumnValue = "SYS_PERMISSION", name = "PK_GEN", table = "TB_GENERATOR", pkColumnName = "GEN_NAME", valueColumnName = "GEN_VALUE", allocationSize = 1)
    @GeneratedValue(generator = "JpaSnowflakeIdGenerator")
    @GenericGenerator(name = "JpaSnowflakeIdGenerator", strategy = "com.github.missthee.db.common.idgenerator.JpaSnowflakeIdGenerator")
    private String id;//主键.
    private String name;//名称.
    private Integer resourceType;//资源类型，[1、menu|2、button]
    private String url;//资源路径.
    private String permission; //权限字符串,menu例子：role:*，button例子：role:create,role:update,role:delete,role:view
    private Boolean available = true;// 是否可用
    @ManyToMany(fetch = FetchType.LAZY)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinTable(name = "sys_permission_note", joinColumns = {@JoinColumn(name = "permission_id", foreignKey = @ForeignKey(name = "none"))}, inverseJoinColumns = {@JoinColumn(name = "note_id", foreignKey = @ForeignKey(name = "none"))})
    private List<SysNote> noteList;
//开启以下映射关系，需要将@Data换为@Setter @Getter，使用@Data，其中@ToString会将双向多对多字段内容死循环遍历输出，导致堆栈溢出错误
//    // 权限 - 角色关系;
//    @ManyToMany
//    @JoinTable(name = "sys_role_permission", joinColumns = {@JoinColumn(name = "permission_id")}, inverseJoinColumns = {@JoinColumn(name = "role_id")}, foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
//    private Set<SysRole> rolePermission;
}