package by.yankavets.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@ComponentScan("by.yankavets")
@PropertySource("classpath:hibernate.properties")
@EnableTransactionManagement
@EnableWebMvc
public class SpringConfiguration implements WebMvcConfigurer {

    private final ApplicationContext applicationContext;
    private final Environment environment;

    public static final String HIBERNATE_DIALECT_PROPERTY = "hibernate.dialect";
    public static final String HIBERNATE_SHOW_SQL_PROPERTY = "hibernate.show_sql";
    public static final String HIBERNATE_DRIVER_CLASS = "hibernate.driver_class";
    public static final String HIBERNATE_CONNECTION_URL = "hibernate.connection.url";
    public static final String HIBERNATE_CONNECTION_USERNAME = "hibernate.connection.username";
    public static final String HIBERNATE_CONNECTION_PASSWORD = "hibernate.connection.password";


    @Autowired
    public SpringConfiguration(ApplicationContext applicationContext, Environment environment) {
        this.applicationContext = applicationContext;
        this.environment = environment;
    }

    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setApplicationContext(applicationContext);
        resolver.setPrefix("classpath:templates/");
        resolver.setSuffix(".html");
        return resolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setTemplateResolver(templateResolver());
        engine.setEnableSpringELCompiler(true);
        return engine;
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine());
        registry.viewResolver(resolver);
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(environment.getRequiredProperty(HIBERNATE_DRIVER_CLASS));
        dataSource.setUrl(environment.getRequiredProperty(HIBERNATE_CONNECTION_URL));
        dataSource.setUsername(environment.getRequiredProperty(HIBERNATE_CONNECTION_USERNAME));
        dataSource.setPassword(environment.getRequiredProperty(HIBERNATE_CONNECTION_PASSWORD));
        return dataSource;
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan("by.yankavets.entity");
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
    }

    @Bean
    public PlatformTransactionManager hibernateTransactionManager() {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory().getObject());
        return transactionManager;
    }


    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put(HIBERNATE_DIALECT_PROPERTY, environment.getRequiredProperty(HIBERNATE_DIALECT_PROPERTY));
        properties.put(HIBERNATE_SHOW_SQL_PROPERTY, environment.getRequiredProperty(HIBERNATE_SHOW_SQL_PROPERTY));
        return properties;
    }
}
