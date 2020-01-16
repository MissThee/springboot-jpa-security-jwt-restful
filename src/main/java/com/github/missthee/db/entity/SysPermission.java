package com.github.missthee.db.entity;

import com.github.missthee.db.common.idgenerator.JpaSnowflakeIdGenerator;
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
@Table
@Setter
@Getter
@Accessors(chain = true)
@NamedEntityGraphs({
        @NamedEntityGraph(
                name = SysPermission.NamedEntityGraph.Graph1,
                attributeNodes = {@NamedAttributeNode(value = SysPermission_.NOTE_LIST, subgraph = SysNote.NamedEntityGraph.Graph1)}
        )
})
@DynamicUpdate()
@DynamicInsert()
public class SysPermission implements Serializable {
    public interface NamedEntityGraph {
        String Graph1 = "SysPermission.Graph1";
    }

    @Id
//    @GeneratedValue(strategy = GenerationType.TABLE, generator = "PK_GEN")
//    @TableGenerator(pkColumnValue = "SYS_PERMISSION", name = "PK_GEN", table = "TB_GENERATOR", pkColumnName = "GEN_NAME", valueColumnName = "GEN_VALUE", allocationSize = 1)
    @GeneratedValue(generator = JpaSnowflakeIdGenerator.NAME)
    @GenericGenerator(name = JpaSnowflakeIdGenerator.NAME, strategy = JpaSnowflakeIdGenerator.STRATEGY)
    private Long id;//主键.
    private String name;//名称.
    private String permission; //权限字符串,menu例子：role:*，button例子：role:create,role:update,role:delete,role:view
    @Column(columnDefinition = "int default 1") //定义默认值
    private Boolean isEnable;// 是否可用
    @ManyToMany(fetch = FetchType.LAZY)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinTable(name = "sys_permission_note",
            joinColumns = {@JoinColumn(name = "permission_id", foreignKey = @ForeignKey(name = "none"))},
            inverseJoinColumns = {@JoinColumn(name = "note_id", foreignKey = @ForeignKey(name = "none"))})
    private List<SysNote> noteList;
//开启以下映射关系，需要将@Data换为@Setter @Getter。使用@Data，其中@ToString会将双向多对多字段内容死循环遍历输出，导致堆栈溢出错误
//    // 权限 - 角色关系;
//    @ManyToMany
//    @JoinTable(name = "sys_role_permission", joinColumns = {@JoinColumn(name = "permission_id")}, inverseJoinColumns = {@JoinColumn(name = "role_id")}, foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
//    private Set<SysRole> rolePermission;
}