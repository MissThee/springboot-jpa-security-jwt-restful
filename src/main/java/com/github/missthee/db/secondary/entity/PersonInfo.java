package com.github.missthee.db.secondary.entity;

import com.fasterxml.jackson.annotation.JsonView;
import com.github.missthee.db.common.idgenerator.JpaSnowflakeIdGenerator;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.Hibernate;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Version;
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
@JsonView(PersonInfo.class)
public class PersonInfo implements Serializable {
    @Id
    @GeneratedValue(generator = JpaSnowflakeIdGenerator.NAME)
    @GenericGenerator(name = JpaSnowflakeIdGenerator.NAME, strategy = JpaSnowflakeIdGenerator.STRATEGY)
    private Long id;//主键.
    private String name;

    @Version
    @ColumnDefault("0")
    private Long version;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        PersonInfo that = (PersonInfo) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}