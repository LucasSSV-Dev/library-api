package com.springdatajpa.libraryapi.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataBaseConfiguration {

    @Value("${spring.datasource.url}")
    String url;
    @Value("${spring.datasource.username}")
    String username;
    @Value("${spring.datasource.password}")
    String password;
    @Value("${spring.datasource.driver-class-name}")
    String driver;


////    @Bean
//    public DataSource dataSource(){
//        DriverManagerDataSource ds = new DriverManagerDataSource();
//        ds.setUrl(url);
//        ds.setUsername(username);
//        ds.setPassword(password);
//        ds.setDriverClassName(driver);
//
//        return ds;
//    }


//    Configuração do hikari:
//    https://github.com/brettwooldridge/hikaricp
    @Bean
    public DataSource hikariDataSource(){
        HikariConfig config = new HikariConfig();
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName(driver);
        config.setJdbcUrl(url);

        //Config maxima do pool pode salvar caso esteja tendo conexões lentas
        config.setMaximumPoolSize(10);// Máximo de conexões liberadas
        config.setMinimumIdle(1);// Tamanho inicial da pool.
        config.setPoolName("library-pool");
        config.setMaxLifetime(600000);// 600 mil ms (aka 10min) passado esse tempo ele desloga.
        config.setConnectionTimeout(100000);

        return new HikariDataSource(config);
    }

}
