package ru.alex.msdeal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@EnableFeignClients
@SpringBootApplication
public class MsDealApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsDealApplication.class, args);
    }

}
