package com.github.missthee.db.common.idgenerator;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Properties;


import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

public class JpaSnowflakeIdGenerator implements Configurable, IdentifierGenerator {
    private Snowflake snowflake;

    @Override
    public void configure(Type type, Properties properties, ServiceRegistry serviceRegistry) throws MappingException {
    }

    @Override
    public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException {
        if (snowflake == null) {
            try {
                snowflake = new Snowflake(0, 0);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return String.valueOf(snowflake.nextId());
    }
}
