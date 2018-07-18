package com.tenmax.db.primary.entity;

import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table
@Getter
@Setter
public class SysRole {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "PK_GEN")
    @TableGenerator(pkColumnValue = "SYS_ROLE", name = "PK_GEN", table = "TB_GENERATOR", pkColumnName = "GEN_NAME", valueColumnName = "GEN_VALUE", allocationSize = 1)
    private Integer id; // 编号
    private String role; // 角色标识程序中判断使用,如"admin",这个是唯一的:
    private String name; // 角色名
    private String description; // 角色描述,UI界面显示使用
    private Boolean available = Boolean.FALSE; // 是否可用

    // 角色 - 权限关系;
    @ManyToMany(fetch = FetchType.LAZY)
    @NotFound(action=NotFoundAction.IGNORE)
    @JoinTable(name = "sys_role_permission", joinColumns = {@JoinColumn(name = "role_id")}, inverseJoinColumns = {@JoinColumn(name = "permission_id")}, foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    private Set<SysPermission> permissionList;
//开启以下映射关系，需要将@Data换为@Setter @Getter，使用@Data，其中@ToString会将双向多对多字段内容死循环遍历输出，导致堆栈溢出错误
//    // 角色 - 用户关系;
//    @ManyToMany
//    @JoinTable(name = "sys_user_role", joinColumns = {@JoinColumn(name = "role_id")}, inverseJoinColumns = {@JoinColumn(name = "user_id")}, foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
//    private Set<SysUser> userRole;// 一个角色对应多个用户

}