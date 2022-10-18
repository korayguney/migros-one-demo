package com.migros.migrosonedemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MigrosOneDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(MigrosOneDemoApplication.class, args);
    }

}
