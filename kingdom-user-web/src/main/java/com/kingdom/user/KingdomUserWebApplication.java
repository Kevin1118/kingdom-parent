package com.kingdom.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ComponentScan(basePackages = {"com.kingdom.pojo","com.kingdom.user"})
public class KingdomUserWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(KingdomUserWebApplication.class, args);
    }

}
