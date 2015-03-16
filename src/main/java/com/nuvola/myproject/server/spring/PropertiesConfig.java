package com.nuvola.myproject.server.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

@Configuration
public class PropertiesConfig {
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer p = new PropertySourcesPlaceholderConfigurer();
        Resource[] resourceLocations = new Resource[]{
                new ClassPathResource("META-INF/goauth.properties"),
                new ClassPathResource("META-INF/corpoauth.properties")
        };
        p.setValueSeparator("=");
        p.setLocations(resourceLocations);
        return p;
    }
}
