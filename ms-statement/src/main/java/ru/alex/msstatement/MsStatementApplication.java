package ru.alex.msstatement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@EnableFeignClients
@SpringBootApplication
public class MsStatementApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsStatementApplication.class, args);
	}

}
