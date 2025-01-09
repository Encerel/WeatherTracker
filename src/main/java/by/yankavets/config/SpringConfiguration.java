package by.yankavets.config;

import by.yankavets.interceptor.LoginInterceptor;
import by.yankavets.interceptor.RedirectIfAuthenticatedInterceptor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.*;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;

import javax.sql.DataSource;
import java.util.Properties;

import static by.yankavets.constant.UrlPath.*;

@Configuration
@ComponentScan("by.yankavets")
@PropertySources({
        @PropertySource("classpath:hibernate.properties"),
        @PropertySource("classpath:weatherApi.properties")}
)
@EnableTransactionManagement
@EnableWebMvc
public class SpringConfiguration implements WebMvcConfigurer {

    private final ApplicationContext applicationContext;
    private final Environment environment;
    private final LoginInterceptor loginInterceptor;
    private final RedirectIfAuthenticatedInterceptor redirectIfAuthenticatedInterceptor;


    public static final String HIBERNATE_DIALECT_PROPERTY = "hibernate.dialect";
    public static final String HIBERNATE_SHOW_SQL_PROPERTY = "hibernate.show_sql";
    public static final String HIBERNATE_DRIVER_CLASS = "hibernate.driver_class";
    public static final String HIBERNATE_CONNECTION_URL = "hibernate.connection.url";
    public static final String HIBERNATE_CONNECTION_USERNAME = "hibernate.connection.username";
    public static final String HIBERNATE_CONNECTION_PASSWORD = "hibernate.connection.password";


    @Autowired
    public SpringConfiguration(ApplicationContext applicationContext, Environment environment, LoginInterceptor loginInterceptor, RedirectIfAuthenticatedInterceptor redirectIfAuthenticatedInterceptor) {
        this.applicationContext = applicationContext;
        this.environment = environment;
        this.loginInterceptor = loginInterceptor;
        this.redirectIfAuthenticatedInterceptor = redirectIfAuthenticatedInterceptor;
    }

    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setApplicationContext(applicationContext);
        resolver.setCharacterEncoding("UTF-8");
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
        resolver.setCharacterEncoding("UTF-8");
        registry.viewResolver(resolver);
    }


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(CSS_URL)
                .addResourceLocations("classpath:/static/css/");

        registry.addResourceHandler(IMAGE_URL)
                .addResourceLocations("classpath:/static/images/");

        registry.addResourceHandler(JS_URL)
                .addResourceLocations("classpath:/static/js/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(redirectIfAuthenticatedInterceptor)
                .addPathPatterns(SIGN_IN_URL, SIGN_UP_URL);

        registry.addInterceptor(loginInterceptor)
                .addPathPatterns(ALL_PAGES_URL_PATTERN)
                .excludePathPatterns(SIGN_IN_URL, SIGN_UP_URL, CSS_URL, JS_URL, IMAGE_URL);

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
        sessionFactory.setPackagesToScan("by.yankavets.model.entity");
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
    }

    @Bean
    public PlatformTransactionManager hibernateTransactionManager() {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory().getObject());
        return transactionManager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }


    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put(HIBERNATE_DIALECT_PROPERTY, environment.getRequiredProperty(HIBERNATE_DIALECT_PROPERTY));
        properties.put(HIBERNATE_SHOW_SQL_PROPERTY, environment.getRequiredProperty(HIBERNATE_SHOW_SQL_PROPERTY));
        return properties;
    }
}
