package ru.alex.msapigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@EnableFeignClients
@SpringBootApplication
public class MsApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsApiGatewayApplication.class, args);
    }

}
