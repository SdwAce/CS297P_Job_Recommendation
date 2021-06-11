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
        dataSource.setUrl("jdbc:postgresql://cs297-instance.ckjk7kzjfhir.us-east-1.rds.amazonaws.com:5432/CS297_Project?createDatabaseIfNotExist=true&serverTimezone=UTC");
        dataSource.setUsername("CS297P");
        dataSource.setPassword("GM755123637qs");

        return dataSource;
    }


    private final Properties hibernateProperties() {
        Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", "update");
        hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQL95Dialect");
        return hibernateProperties;
    }
}

