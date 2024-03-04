package br.com.iug;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ExpenseControlApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExpenseControlApplication.class, args);
	}

}
