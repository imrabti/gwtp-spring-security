package com.nuvola.myproject.server.spring;

import javax.sql.DataSource;

import org.h2.jdbcx.JdbcDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

@Configuration
@Import(PropertiesConfig.class)
public class PersistenceConfig {
    private static final String H2_JDBC_URL_TEMPLATE = "jdbc:h2:%s/target/db/sample;AUTO_SERVER=TRUE";
    @Value("classpath:/META-INF/providerconnectionschema.sql")
    private Resource H2_SCHEMA_SCRIPT;

    @Bean
    public DataSource dataSource() throws Exception {
        String jdbcUrl = String.format(H2_JDBC_URL_TEMPLATE, System.getProperty("user.dir"));
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL(jdbcUrl);
        dataSource.setUser("oauth-client");
        dataSource.setPassword("oauth-client");
        return dataSource;
    }

    @Autowired
    @Bean
    public DataSourceInitializer dataSourceInitializer(final DataSource dataSource) {
        final DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(dataSource);
        initializer.setDatabasePopulator(databasePopulator());
        return initializer;
    }

    private DatabasePopulator databasePopulator() {
        final ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(H2_SCHEMA_SCRIPT);
        return populator;
    }
}
