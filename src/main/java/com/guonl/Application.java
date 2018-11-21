package com.guonl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ImportResource({"classpath:spring/application-*.xml"})
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	/*@Bean
	@Primary
//	@ConfigurationProperties("spring.datasource")
	public DataSourceProperties primaryDataSourceProperties() {
		return new DataSourceProperties();
	}

	@Bean
	@Primary
	public DataSource primaryDataSource(@Autowired DataSourceProperties props) {
		return props.initializeDataSourceBuilder().build();
	}

	@Bean
	@Primary
	public JdbcTemplate primaryJdbcTemplate(DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}*/

}
