package com.kingdom.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class KingdomUserWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(KingdomUserWebApplication.class, args);
    }

}
