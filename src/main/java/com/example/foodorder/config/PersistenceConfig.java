package com.example.foodorder.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement // Habilita el manejo de transacciones de Spring (@Transactional)
@PropertySource("classpath:database.properties") // Carga propiedades desde src/main/resources/database.properties
public class PersistenceConfig {

    @Autowired
    private Environment env; // Inyecta el entorno para leer las propiedades

    // Configura el DataSource (Pool de Conexiones DBCP2)
    @Bean(destroyMethod = "close") // Asegura que el pool se cierre correctamente
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(env.getProperty("db.driver"));
        dataSource.setUrl(env.getProperty("db.url"));
        dataSource.setUsername(env.getProperty("db.username"));
        dataSource.setPassword(env.getProperty("db.password"));
        // Configuraciones opcionales del pool
        // dataSource.setInitialSize(5);
        // dataSource.setMaxTotal(20);
        // dataSource.setMinIdle(5);
        // dataSource.setMaxWaitMillis(10000);
        return dataSource;
    }

    // Configura el SessionFactory de Hibernate a través de Spring
    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        // Paquete(s) donde se encuentran las clases anotadas con @Entity
        sessionFactory.setPackagesToScan("com.example.foodorder.model");
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
    }

    // Configura el Administrador de Transacciones de Hibernate para Spring
    @Bean
    public PlatformTransactionManager hibernateTransactionManager() {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        // Necesita obtener el objeto SessionFactory del LocalSessionFactoryBean
        transactionManager.setSessionFactory(sessionFactory().getObject());
        return transactionManager;
    }

    // Propiedades específicas de configuración para Hibernate
    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto", "validate")); // Valor por defecto 'validate'
        properties.setProperty("hibernate.dialect", env.getProperty("hibernate.dialect"));
        properties.setProperty("hibernate.show_sql", env.getProperty("hibernate.show_sql", "false"));
        properties.setProperty("hibernate.format_sql", env.getProperty("hibernate.format_sql", "false"));
        // Añadir más propiedades si es necesario (ej. caché de segundo nivel)
        // properties.setProperty("hibernate.cache.use_second_level_cache", "false");
        return properties;
    }
}
