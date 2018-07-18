package com.tenmax.db.primary.entity;


import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table
@Getter
@Setter
@NamedEntityGraph(
        name = "SysUser.g1",
        attributeNodes = {
                @NamedAttributeNode(value = "roleList", subgraph = "userRoleGraph"),
        },
        subgraphs = {
                @NamedSubgraph(name = "userRoleGraph",
                        attributeNodes = {
                                @NamedAttributeNode(value = "permissionList", subgraph = "rolePermissionGraph")}),
                @NamedSubgraph(name = "rolePermissionGraph",
                        attributeNodes = {
                                @NamedAttributeNode(value = "permission")})
        }
)
@DynamicUpdate()
public class SysUser implements Serializable {

    public interface UserPasswordView {
    }// 视图

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "PK_GEN")
    @TableGenerator(pkColumnValue = "SYS_USER", name = "PK_GEN", table = "TB_GENERATOR", pkColumnName = "GEN_NAME", valueColumnName = "GEN_VALUE", allocationSize = 1)
    private Integer id;
    @Column(unique = true)
    private String username; //帐号
    private String nickname; //昵称

    @JsonView(UserPasswordView.class)
    private String password; //密码;
    private byte state;      //用户状态

    // 用户 - 角色关系;
    @ManyToMany(fetch = FetchType.LAZY)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinTable(name = "sys_user_role", joinColumns = {@JoinColumn(name = "user_id")}, inverseJoinColumns = {@JoinColumn(name = "role_id")}, foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    private Set<SysRole> roleList;

}



