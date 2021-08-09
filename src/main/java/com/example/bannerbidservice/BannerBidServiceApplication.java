package com.example.bannerbidservice;

import com.example.bannerbidservice.config.Config;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@SpringBootApplication
public class BannerBidServiceApplication {
    static ConfigurableApplicationContext appCtx;
    static Boolean active;

    static {
        Properties properties = new Properties();

        try {
            FileInputStream fileInputStream = new FileInputStream(Config.ATTRIBUTE_FILE_NAME);

            properties.load(fileInputStream);
            boolean isAllAttributes = Config.DATA_SOURCE_ATTRIBUTES.containsAll(properties.keySet());
            if (!isAllAttributes) {
                System.err.println("Не все атрибуты подключения к БД заданы");
                active = false;
            }
        } catch (IOException e) {
            System.err.println("Не удалось прочитать файл " + Config.ATTRIBUTE_FILE_NAME);
            active = false;
        }
        active = true;
    }

    public static void main(String[] args) {
        if (!active) {
            System.err.println("Ошибка запуска сервиса");
        } else {

            appCtx = SpringApplication.run(BannerBidServiceApplication.class, args);
        }
    }

    public static void shutdown() {
        appCtx.close();
        System.exit(SpringApplication.exit(appCtx, () -> -1));
    }

    public static void restart() {
        ApplicationArguments args = appCtx.getBean(ApplicationArguments.class);

        Thread thread = new Thread(() -> {
            appCtx.close();
            appCtx = SpringApplication.run(BannerBidServiceApplication.class, args.getSourceArgs());
        });

        thread.setDaemon(false);
        thread.start();
    }
}