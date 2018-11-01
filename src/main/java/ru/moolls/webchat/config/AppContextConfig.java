package ru.moolls.webchat.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.moolls.webchat.dao.domain.User;
import ru.moolls.webchat.dao.domain.enums.UserRoleEnum;
import ru.moolls.webchat.security.MethodPermissionResolver;

import javax.sql.DataSource;

@Configuration
@PropertySource({"classpath:dbconfig.properties"})
public class AppContextConfig {

    @Value("${driver}")
    private String jdbcDriver;
    @Value("${url}")
    private String jdbcUrl;
    @Value("${user}")
    private String jdbcUser;
    @Value("${password}")
    private String jdbcPassword;

    @Bean
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(jdbcDriver);
        dataSource.setUrl(jdbcUrl);
        dataSource.setUsername(jdbcUser);
        dataSource.setPassword(jdbcPassword);
        return dataSource;
    }

    @Bean
    public MethodPermissionResolver permissionAnnotationHandler() {
        return new MethodPermissionResolver();
    }

    @Bean
    public JdbcTemplate jdbcTemplate(@Autowired DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    @Scope("prototype")
    public User user() {
        User user = new User();
        user.setUserRole(UserRoleEnum.ANONYMOUS.getValue());
        return user;
    }
}
