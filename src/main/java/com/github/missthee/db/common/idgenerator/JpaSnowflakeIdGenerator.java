package com.github.missthee.db.common.idgenerator;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Properties;


import lombok.SneakyThrows;
import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

public class JpaSnowflakeIdGenerator implements Configurable, IdentifierGenerator {
    private static Snowflake snowflake;
    public static final String NAME ="Jpa_Snow_flake_Id_Generator";
    public static final String STRATEGY ="com.github.missthee.db.common.idgenerator.JpaSnowflakeIdGenerator";
    @Override
    public void configure(Type type, Properties properties, ServiceRegistry serviceRegistry) throws MappingException {
    }

    @SneakyThrows
    @Override
    public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException {
        if (snowflake == null) {
                snowflake = new Snowflake(0, 0);
        }
        return snowflake.nextId();
    }
}
