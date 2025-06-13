package com.yc.smartdb.config;

import com.yc.smartdb.commands.MyCommand;
import com.yc.smartdb.model.AiProperties;
import com.yc.smartdb.model.OllamaResponse;
import com.yc.smartdb.repository.GenericDao;
import com.yc.smartdb.service.ApplicationService;
import com.yc.smartdb.service.LocalOllamaService;
import com.yc.smartdb.service.SchemaExtractorService;
import com.yc.smartdb.service.VannaService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.client.RestTemplate;

@Configuration
@ComponentScan(basePackages = "com.yc.smartdb")
public class AppConfig {

    @Bean
    public MyCommand myCommand(ApplicationService applicationService,LocalOllamaService localOllamaService,VannaService vannaService) {
        return new MyCommand(applicationService,localOllamaService,vannaService);
    }
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    };


    @Bean
    public ApplicationService applicationService(GenericDao genericDao) {
        return new ApplicationService(genericDao);
    }
    @Bean
    public LocalOllamaService localOllamaService(RestTemplate restTemplate){
        return new LocalOllamaService(restTemplate);
    }
    @Bean
    public VannaService vannaService(){
        return new VannaService();
    }

    @Bean
    public SchemaExtractorService schemaExtractorService(){
        return new SchemaExtractorService();
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

   @Bean
    public AiProperties getAiProperties(){
        return new AiProperties();
    }

}
