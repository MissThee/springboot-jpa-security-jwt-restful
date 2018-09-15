package com.server.db.primary.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table
@Getter
@Setter
public class SysPermission implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "PK_GEN")
    @TableGenerator(pkColumnValue = "SYS_PERMISSION", name = "PK_GEN", table = "TB_GENERATOR", pkColumnName = "GEN_NAME", valueColumnName = "GEN_VALUE", allocationSize = 1)
    private Integer id;//主键.
    private String name;//名称.
    private Integer resourceType;//资源类型，[1、menu|2、button]
    private String url;//资源路径.
    private String permission; //权限字符串,menu例子：role:*，button例子：role:create,role:update,role:delete,role:view
    private Boolean available = Boolean.FALSE;// 是否可用
//开启以下映射关系，需要将@Data换为@Setter @Getter，使用@Data，其中@ToString会将双向多对多字段内容死循环遍历输出，导致堆栈溢出错误
//    // 权限 - 角色关系;
//    @ManyToMany
//    @JoinTable(name = "sys_role_permission", joinColumns = {@JoinColumn(name = "permission_id")}, inverseJoinColumns = {@JoinColumn(name = "role_id")}, foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
//    private Set<SysRole> rolePermission;
}