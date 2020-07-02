package com.kingdom.user.service;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


//开启Dubbo服务注解
@EnableDubbo
@SpringBootApplication
@MapperScan(basePackages = "com.kingdom.dao")
public class KingdomUserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(KingdomUserServiceApplication.class, args);
    }

}
