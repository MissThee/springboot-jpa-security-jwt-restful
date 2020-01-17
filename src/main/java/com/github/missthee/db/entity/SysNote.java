package com.github.missthee.db.entity;

import com.fasterxml.jackson.annotation.JsonView;
import com.github.missthee.db.common.idgenerator.JpaSnowflakeIdGenerator;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Setter
@Getter
@Accessors(chain = true)
@DynamicUpdate()//更新时update语句只构建变动的字段（即数据库中查出的数据与当前实体类中差异的部分）
@DynamicInsert()//插入时insert语句只构建变动的字段（即数据库中查出的数据与当前实体类中差异的部分，因插入时数据库一般查不到数据，所有字段认为是null。）
@JsonView(SysNote.class)
public class SysNote implements Serializable {
    public interface NamedEntityGraph {
        String Graph1 = "SysNote.Graph1";
    }

    //视图。
    //定义：通过在实体类属性上使用@JsonView()来指定本视图的字段；
    //使用：1、通过在Controller中使用@JsonView改变输出的字段：与当前视图匹配的字段 和 没有加@JsonView的字段 将会被返回
    //     2、若希望仅输出 与当前视图匹配的字段 ，可在类上使用@JsonView()注解，将整个类中属性分配默认的 视图
    public interface first2Param {
    }

    public interface first4Param extends first2Param {
    }

    @Id
    //1、table主键生成策略
    //   创建一个新表，记录每个表中的主键，每次从这个表获取、更新主键
//    @GeneratedValue(strategy = GenerationType.TABLE, generator = "PK_GEN")
//    @TableGenerator(pkColumnValue = "SYS_NOTE", name = "PK_GEN", table = "TB_GENERATOR", pkColumnName = "GEN_NAME", valueColumnName = "GEN_VALUE", allocationSize = 1)
    //2、自定义主键生成器
    //   创建自己的主键生成器，继承IdentifierGenerator
    @GeneratedValue(generator = JpaSnowflakeIdGenerator.NAME)
    @GenericGenerator(name = JpaSnowflakeIdGenerator.NAME, strategy = JpaSnowflakeIdGenerator.STRATEGY)
    @JsonView(first2Param.class)
    private Long id;//主键.
    private String note;
    @JsonView(first2Param.class)
    private String param1;
    @JsonView(first2Param.class)
    private String param2;
    @JsonView(first4Param.class)
    private String param3;
    @JsonView(first4Param.class)
    private String param4;
    private String param5;


    @ColumnDefault("12")
    private Byte byte1;
    @ColumnDefault("2")
    private Short short1;
    @ColumnDefault("2")
    private Integer integer1;
    @ColumnDefault("2")
    private Long long1;
    @ColumnDefault("1.75")
    private Float float1;
    @ColumnDefault("1.25")//设置建表时，本字段默认值
    private Double double1;
    @ColumnDefault("1")
    private Boolean boolean1;
    @ColumnDefault("'A'")
    private Character character1;


}