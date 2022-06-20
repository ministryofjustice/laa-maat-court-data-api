package gov.uk.courtdata.config;

import com.amazonaws.xray.sql.TracingDataSource;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
@ConditionalOnProperty(prefix = "xray", value = "enabled", havingValue = "true")
public class AWSXRayHikariConfig {

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource")
    public DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "hikariDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    public HikariDataSource hikariDataSource(DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Primary
    @Bean(name = "dataSource")
    public DataSource dataSource(HikariDataSource hikariDataSource) {
        return TracingDataSource.decorate(hikariDataSource);
    }

}
