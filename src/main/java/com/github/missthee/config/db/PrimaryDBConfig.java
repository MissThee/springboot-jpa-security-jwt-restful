package com.github.missthee.config.db;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.List;
import java.util.Objects;
@Configuration
@ConditionalOnProperty(name = "spring.datasource.primary.enable", havingValue = "true")
//@EnableTransactionManagement//org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration中已配置
@EnableConfigurationProperties({HibernateProperties.class, JpaProperties.class})
public class PrimaryDBConfig {
    public static final String DB_INDEX_NAME = "primary";
    public static final String DB_REPOSITORY_PACKAGE = "com.github.missthee.db." + DB_INDEX_NAME + ".repository";
    public static final String DB_ENTITY_PACKAGE = "com.github.missthee.db." + DB_INDEX_NAME + ".entity";

    @EnableJpaRepositories(
            entityManagerFactoryRef = DB_INDEX_NAME + "EntityManagerFactory",
            transactionManagerRef = DB_INDEX_NAME + "TransactionManager",
            basePackages = {DB_REPOSITORY_PACKAGE})//设置dao（repo）所在位置
    private static class EnableJpaRepositoriesConfig {

    }

    @Bean(name = DB_INDEX_NAME + "DataSource")
    @ConfigurationProperties("spring.datasource." + DB_INDEX_NAME)
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = DB_INDEX_NAME + "EntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean(JpaProperties jpaProperties, HibernateProperties hibernateProperties) {
        return new LocalContainerEntityManagerFactoryBean() {{
            setJpaPropertyMap(hibernateProperties.determineHibernateProperties(
                    jpaProperties.getProperties(),
                    new HibernateSettings() {{
                        ddlAuto(hibernateProperties::getDdlAuto);
                    }}));
            setJpaVendorAdapter(new HibernateJpaVendorAdapter() {{
                setShowSql(jpaProperties.isShowSql());
                if (jpaProperties.getDatabase() != null) {
                    setDatabase(jpaProperties.getDatabase());
                }
                if (jpaProperties.getDatabasePlatform() != null) {
                    setDatabasePlatform(jpaProperties.getDatabasePlatform());
                }
                setGenerateDdl(jpaProperties.isGenerateDdl());
            }});
            //配置PersistenceUnit别名，默认可用本bean的名称，配置后可使用此名称。常用于@PersistenceUnit和@PersistenceContext注解的unitName属性
            setPersistenceUnitName(DB_INDEX_NAME + "PersistenceUnit");
            setDataSource(dataSource());
            setPackagesToScan(DB_ENTITY_PACKAGE);
            List<String> mappingResources = jpaProperties.getMappingResources();
            setMappingResources((!ObjectUtils.isEmpty(mappingResources) ? StringUtils.toStringArray(mappingResources) : null));
        }};
    }

    @Bean(name = DB_INDEX_NAME + "TransactionManager")
    PlatformTransactionManager platformTransactionManager(@Qualifier(DB_INDEX_NAME + "EntityManagerFactory") LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean, ObjectProvider<TransactionManagerCustomizers> transactionManagerCustomizers) {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager(Objects.requireNonNull(localContainerEntityManagerFactoryBean.getObject()));
        transactionManagerCustomizers.ifAvailable((customizers) -> customizers.customize(jpaTransactionManager));
        return jpaTransactionManager;
    }

//    使用@PersistenceContext(unitName = "primaryEntityManagerFactory")获取自动注册的EntityManager，不自己注册bean
//    @Bean(name = DB_INDEX_NAME + "EntityManager")
//    public EntityManager entityManager(@Qualifier(DB_INDEX_NAME + "EntityManagerFactory") LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean) {
//        //若要自行注册EntityManager，需使用SharedEntityManagerCreator工具类注册
//        return SharedEntityManagerCreator.createSharedEntityManager(Objects.requireNonNull(localContainerEntityManagerFactoryBean.getObject()));
//    }
}

