package com.github.missthee.db.entity;


import com.fasterxml.jackson.annotation.JsonView;
import com.github.missthee.db.common.idgenerator.JpaSnowflakeIdGenerator;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Set;


@Entity
@Table
@Setter
@Getter
@Accessors(chain = true)
@NamedEntityGraphs({
        @NamedEntityGraph(
                name = SysUser.NamedEntityGraph.Graph1,
                //此处的subgraph名称使用的是subgraphs中的名称，即使其他实体类中有NamedEntityGraph同名也没有任何关系
                attributeNodes = {@NamedAttributeNode(value = SysUser_.ROLE_LIST, subgraph = SysRole.NamedEntityGraph.Graph1)},
                //user关联2个以上role开始出现 n+1问题，每多一个role多查询一次，使用主键查
                subgraphs = {
                        @NamedSubgraph(
                                //role关联1个以上permission开始出现 n+1问题，每多一个permission多查询一次，使用主键查
                                name = SysRole.NamedEntityGraph.Graph1,
                                attributeNodes = {@NamedAttributeNode(value = SysRole_.PERMISSION_LIST, subgraph = SysPermission.NamedEntityGraph.Graph1)}),
                        @NamedSubgraph(
                                //permission关联1个以note上开始出现 n+1问题，每多一个permission多查询一次，使用主键查
                                name = SysPermission.NamedEntityGraph.Graph1,
                                attributeNodes = {@NamedAttributeNode(value = SysPermission_.NOTE_LIST)}),
                }
        )
})
@DynamicUpdate()//更新时update语句只构建变动的字段（即数据库中查出的数据与当前实体类中差异的部分）
@DynamicInsert()//插入时insert语句只构建变动的字段（即数据库中查出的数据与当前实体类中差异的部分，因插入时数据库一般查不到数据，所有字段认为是null。）
public class SysUser implements Serializable {
    public interface NamedEntityGraph {
        String Graph1 = "SysUser.Graph1";
    }

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
    @Column(columnDefinition = "int default 1") //定义默认值
    private Boolean isEnable;      //用户状态

    // 用户 - 角色关系;
    @ManyToMany(fetch = FetchType.LAZY)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinTable(name = "sys_user_role",
            joinColumns = {@JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "none"))},
            inverseJoinColumns = {@JoinColumn(name = "role_id", foreignKey = @ForeignKey(name = "none"))})
    private Set<SysRole> roleList;
}



