package com.yc.smartdb.config;

import com.yc.smartdb.commands.MyCommand;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
@ComponentScan(basePackages = "com.yc.smartdb")
public class AppConfig {

    @Bean
    public MyCommand myCommand(){
        return new MyCommand();
    }

    @Bean
    public JdbcTemplate jdbcTemplate(){
        return new JdbcTemplate(driverManagerDataSource());
    }

    @Bean
    public DriverManagerDataSource driverManagerDataSource(){
        return new DriverManagerDataSource("jdbc:mysql://localhost:3306/student","root","");
    }
}
