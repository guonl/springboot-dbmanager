package com.guonl.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import javax.sql.DataSource;

/**
 * Created by guonl
 * Date 2018/11/19 4:53 PM
 * Description: 获取JdbcTemplate
 */
@Configuration
public class JdbcTemplateConfig {

    @Bean
    public JdbcTemplate getJdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

}
