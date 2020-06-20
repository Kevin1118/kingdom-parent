package com.kingdom.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class KingdomProductWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(KingdomProductWebApplication.class, args);
    }

}
