package co.jp.arche1.kdrs.common.security.config;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.sql.DataSource;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class DatabaseConfig  {
  @Autowired
  private Environment env;

  private DataSource kpmsDataSource() {
	  DriverManagerDataSource dataSource = new DriverManagerDataSource();
	  dataSource.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
	  dataSource.setUrl(env.getProperty("spring.datasource.url"));
	  dataSource.setUsername(env.getProperty("spring.datasource.username"));
	  dataSource.setPassword(env.getProperty("spring.datasource.password"));
	  return dataSource;
  }

  private DataSource kdrsDataSource() {
	  DriverManagerDataSource dataSource = new DriverManagerDataSource();
	  dataSource.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
	  dataSource.setUrl("jdbc:mysql://localhost/ikodb?characterEncoding=UTF-8&serverTimezone=Asia/Tokyo");
	  dataSource.setUsername("root");
	  dataSource.setPassword("password");
	  return dataSource;
  }


  /**
   * dataSourceのdefaultを設定するBean.
   *
   * @return DynamicRoutingDataSourceResolver
   */
  @Bean
  @Primary
  public DynamicRoutingDataSourceResolver dataSourceResolver() {
    DynamicRoutingDataSourceResolver resolver = new DynamicRoutingDataSourceResolver();

    Map<Object, Object> dataSources = new LinkedHashMap<>();
    dataSources.put("kpms", kpmsDataSource());
    dataSources.put("kdrs", kdrsDataSource());

    resolver.setTargetDataSources(dataSources);
    resolver.setDefaultTargetDataSource(kpmsDataSource());

    return resolver;
  }

}
