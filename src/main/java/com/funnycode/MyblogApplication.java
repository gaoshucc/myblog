package com.funnycode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
/**
 * @author gaoshucc
 */
@EnableTransactionManagement
@SpringBootApplication
public class MyblogApplication {
	public static void main(String[] args) {
		SpringApplication.run(MyblogApplication.class, args);
	}
}
