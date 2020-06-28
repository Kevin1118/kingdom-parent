package com.kingdom.consultant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class )
@ComponentScan(basePackages = {"com.kingdom.pojo","com.kingdom.consultant"})
public class KingdomConsultantWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(KingdomConsultantWebApplication.class, args);
    }

}
