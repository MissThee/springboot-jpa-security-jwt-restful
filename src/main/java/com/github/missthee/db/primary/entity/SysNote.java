package com.github.missthee.db.primary.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table
@Setter
@Getter
@Accessors(chain = true)
public class SysNote implements Serializable {
    @Id
    @GeneratedValue(generator = "JpaSnowflakeIdGenerator")
    @GenericGenerator(name = "JpaSnowflakeIdGenerator", strategy = "com.github.missthee.db.common.IdGenerator.JpaSnowflakeIdGenerator")
    private String id;//主键.
    private String note;
}