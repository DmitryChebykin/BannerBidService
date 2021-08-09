package com.example.bannerbidservice.config;

import com.example.bannerbidservice.BannerBidServiceApplication;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import javax.sql.DataSource;
import javax.validation.Validator;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

@Configuration
@EnableScheduling
public class Config {
    public static final List<String> DATA_SOURCE_ATTRIBUTES = Arrays.asList("db.user", "db.password",
            "db.address", "db.port", "db.name");
    public static final String ATTRIBUTE_FILE_NAME = "mysql.properties";

    @Bean
    public Validator validator() {
        return new LocalValidatorFactoryBean();
    }

    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        MethodValidationPostProcessor methodValidationPostProcessor = new MethodValidationPostProcessor();
        methodValidationPostProcessor.setValidator(validator());
        return methodValidationPostProcessor;
    }

    @Bean
    public DataSource getDataSource() {
        Properties properties = new Properties();

        try {
            FileInputStream fileInputStream = new FileInputStream(ATTRIBUTE_FILE_NAME);

            properties.load(fileInputStream);
            boolean isAllAttributes = DATA_SOURCE_ATTRIBUTES.containsAll(properties.keySet());
            if (!isAllAttributes) {
                System.out.println("Не все атрибуты подключения к БД заданы");
                BannerBidServiceApplication.shutdown();
            }
        } catch (IOException e) {
            System.out.println("Не удалось прочитать файл " + ATTRIBUTE_FILE_NAME);
            BannerBidServiceApplication.shutdown();
        }

        String dbAddress = properties.getProperty("db.address");
        String dbPort = properties.getProperty("db.port");
        String dbName = properties.getProperty("db.name");
        String dbUser = properties.getProperty("db.user");
        String dbPassword = properties.getProperty("db.password");

        return getDataSource(dbAddress, dbPort, dbName, dbUser, dbPassword);
    }

    private DataSource getDataSource(String dbAddress, String dbPort, String dbName, String dbUser, String dbPassword) {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.username(dbUser);
        dataSourceBuilder.password(dbPassword);
        //spring.datasource.url = jdbc:mysql://localhost:3306/banners_app?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&useLegacyDatetimeCode=false
        String url = "jdbc:mysql://" + dbAddress + ":" + dbPort + "/" + dbName + "?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&useLegacyDatetimeCode=false";
        dataSourceBuilder.url(url);
        return dataSourceBuilder.build();
    }
}