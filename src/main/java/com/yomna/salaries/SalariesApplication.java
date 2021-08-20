package com.yomna.salaries;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SalariesApplication {

    public static void main(String[] args) {
        SpringApplication.run(SalariesApplication.class, args);

        System.out.println("Application Started !");
    }

}
