package com.crud.crud_lombok_dto;

import com.crud.crud_lombok_dto.test.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//@EnableScheduling
public class CrudLombokDtoApplication {

	public static void main(String[] args) {
		SpringApplication.run(CrudLombokDtoApplication.class, args);
		//Test.executeTaskEveryMinute();
	}
}

