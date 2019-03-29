package com.github.missthee.db.primary.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Entity
@Table
@Setter
@Getter
@Accessors(chain = true)
@NamedEntityGraphs({
        @NamedEntityGraph(
                name = "SysUser.g1",
                attributeNodes = {@NamedAttributeNode(value = "roleList", subgraph = "permissionGraph")},
                subgraphs = {
                        @NamedSubgraph(
                                name = "permissionGraph",
                                attributeNodes = {@NamedAttributeNode(value = "permissionList", subgraph = "noteGraph")}),
                        @NamedSubgraph(
                                name = "noteGraph",
                                attributeNodes = {@NamedAttributeNode(value = "noteList")})
                }
        )
})
@DynamicUpdate()//更新时update语句只更新变动的字段
public class SysUser implements Serializable {
    public interface UserPasswordView {
    }// 视图

    @Id
//    @GeneratedValue(strategy = GenerationType.TABLE, generator = "PK_GEN")
//    @TableGenerator(pkColumnValue = "SYS_USER", name = "PK_GEN", table = "TB_GENERATOR", pkColumnName = "GEN_NAME", valueColumnName = "GEN_VALUE", allocationSize = 1)
    @GeneratedValue(generator = "JpaSnowflakeIdGenerator")
    @GenericGenerator(name = "JpaSnowflakeIdGenerator", strategy = "com.github.missthee.db.common.idgenerator.JpaSnowflakeIdGenerator")
    private String id;
    @Column(unique = true)
    private String username; //帐号
    private String nickname; //昵称

    @JsonView(UserPasswordView.class)
    private String password; //密码;
    private byte state;      //用户状态

    // 用户 - 角色关系;
    @ManyToMany(fetch = FetchType.LAZY)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinTable(name = "sys_user_role", joinColumns = {@JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "none"))}, inverseJoinColumns = {@JoinColumn(name = "role_id", foreignKey = @ForeignKey(name = "none"))})
    private Set<SysRole> roleList;
}



