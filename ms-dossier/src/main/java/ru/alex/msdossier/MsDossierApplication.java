package ru.alex.msdossier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;


@EnableAsync
@SpringBootApplication
public class MsDossierApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsDossierApplication.class, args);
	}

}
