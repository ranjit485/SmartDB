package com.yc.smartdb.config;

import com.yc.smartdb.commands.MyCommand;
import com.yc.smartdb.model.DatabaseProps;
import com.yc.smartdb.repository.GenericDao;
import com.yc.smartdb.service.ApplicationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
@ComponentScan(basePackages = "com.yc.smartdb")
public class AppConfig {

    @Bean
    public MyCommand myCommand(ApplicationService applicationService) {
        return new MyCommand(applicationService);
    }

    @Bean
    public ApplicationService applicationService(GenericDao genericDao) {
        return new ApplicationService(genericDao);
    }

    @Bean
    public GenericDao getGenericDao() {
        return new GenericDao(getJdbcTemplate());
    }

    @Bean
    public DriverManagerDataSource dataSource() {
        return new DriverManagerDataSource();
    }

    @Bean
    public JdbcTemplate getJdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }
}
