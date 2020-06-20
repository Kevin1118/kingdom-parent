package com.kingdom.consultant.service;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableDubbo
@SpringBootApplication
@MapperScan(basePackages = "com.kingdom.dao")
public class KingdomConsultantServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(KingdomConsultantServiceApplication.class, args);
    }

}
