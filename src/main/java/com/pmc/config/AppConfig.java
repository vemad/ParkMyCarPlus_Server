package com.pmc.config;

import com.pmc.model.LogPlace;
import com.pmc.model.Place;
import com.pmc.model.User;
import com.pmc.model.Zone;
import org.apache.commons.dbcp.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories
public class AppConfig {

    private static final String DATASOURCE_DRIVER_CLASS_NAME = "spring.datasource.driver-class-name";
    private static final String DATASOURCE_URL = "spring.datasource.url";
    private static final String DATASOURCE_USERNAME = "spring.datasource.username";
    private static final String DATASOURCE_PASSWORD = "spring.datasource.password";

    @Autowired
    private Environment env;

    @Bean
    public HibernateTemplate hibernateTemplate() {
        return new HibernateTemplate(sessionFactory());
    }

    @Bean
    public SessionFactory sessionFactory() {
        return new LocalSessionFactoryBuilder(getDataSource())
                .addAnnotatedClasses(Place.class)
                .addAnnotatedClasses(LogPlace.class)
                .addAnnotatedClasses(Zone.class)
                .addAnnotatedClasses(User.class)
                .buildSessionFactory();
    }

    @Bean
    public DataSource getDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(env.getProperty(DATASOURCE_DRIVER_CLASS_NAME));
        dataSource.setUrl(env.getProperty(DATASOURCE_URL));
        dataSource.setUsername(env.getProperty(DATASOURCE_USERNAME));
        dataSource.setPassword(env.getProperty(DATASOURCE_PASSWORD));

        return dataSource;
    }
}
