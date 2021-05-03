package Database;

import java.util.Properties;
import javax.sql.DataSource;

import Database.RDSConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

@Configuration
public class ApplicationConfig {

    @Bean(name = "sessionFactory")
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();//create bean
        sessionFactory.setDataSource(dataSource()); //set data source, postgressql
        sessionFactory.setPackagesToScan("Model");
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
    }

    @Bean(name = "dataSource")
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://database-1.crmpg9f2oru7.us-east-1.rds.amazonaws.com:5432/Users?createDatabaseIfNotExist=true&serverTimezone=UTC");
        dataSource.setUsername("CS297_instance");
        dataSource.setPassword("GM755123637qs");

        return dataSource;
    }


    private final Properties hibernateProperties() {
        Properties hibernateProperties = new Properties();
        //hibernateProperties.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQL95Dialect");
        return hibernateProperties;
    }
}

