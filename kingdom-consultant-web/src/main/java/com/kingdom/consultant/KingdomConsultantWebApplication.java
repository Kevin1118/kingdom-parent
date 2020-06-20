package com.kingdom.consultant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class KingdomConsultantWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(KingdomConsultantWebApplication.class, args);
    }

}
