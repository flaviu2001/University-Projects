package server.config;


import org.apache.commons.dbcp2.BasicDataSource;
import org.postgresql.Driver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;
import java.util.stream.Stream;

@Configuration
public class JdbcConfig {
    private HashMap<String, String> readSettingsFile() {
        HashMap<String, String> propertiesMap = new HashMap<>();
        Properties properties = new Properties();

        String configFile = "data/programData/settings.properties";
        FileInputStream fileInputStream;
        try {
            fileInputStream = new FileInputStream(configFile);
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
            return propertiesMap;
        }
        Stream.ofNullable(fileInputStream).findAny().ifPresentOrElse((el) ->  {
            try {
                properties.load(fileInputStream);
                propertiesMap.put("database", properties.getProperty("database"));
                propertiesMap.put("user", properties.getProperty("user"));
                propertiesMap.put("password", properties.getProperty("password"));
            } catch (IOException ioException) {
                System.out.println("IOException: " + ioException.getMessage());
            }
        }, () -> System.out.println("Invalid config file"));

        return propertiesMap;
    }

    @Bean
    JdbcOperations jdbcOperations(){
        JdbcTemplate jdbcTemplate=new JdbcTemplate();
        jdbcTemplate.setDataSource(dataSource());
        return jdbcTemplate;
    }

    @Bean
    DataSource dataSource() {
        BasicDataSource dataSource=new BasicDataSource();
        dataSource.setDriverClassName(Driver.class.getName());
        HashMap<String, String> configurations = readSettingsFile();
        dataSource.setUsername(configurations.get("user"));
        dataSource.setPassword(configurations.get("password"));
        dataSource.setUrl(configurations.get("database"));
        dataSource.setInitialSize(2);
        return dataSource;
    }
}
