package com.vix.haiguitang;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.vix.haiguitang.mapper")
@SpringBootApplication
public class HaiguitangApplication {

    public static void main(String[] args) {
        SpringApplication.run(HaiguitangApplication.class, args);
    }

}
