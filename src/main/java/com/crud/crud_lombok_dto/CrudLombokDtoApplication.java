package com.crud.crud_lombok_dto;

import com.crud.crud_lombok_dto.config.RoleType;
import com.crud.crud_lombok_dto.model.Role;
import com.crud.crud_lombok_dto.repository.RoleRepository;
import com.crud.crud_lombok_dto.test.Test;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
//@EnableScheduling
public class CrudLombokDtoApplication {

	public static void main(String[] args) {
		SpringApplication.run(CrudLombokDtoApplication.class, args);
		//Test.executeTaskEveryMinute();
	}

	@Bean
	public CommandLineRunner initRoles(RoleRepository roleRepo) {
		return args -> {
			try {
				if (roleRepo.count() == 0) {
					List<Role> roles = Arrays.stream(RoleType.values())
							.map(Role::new)
							.toList();
					roleRepo.saveAll(roles);
					roles.forEach(r -> System.out.println("Inserted: " + r.getName()));
				}
			} catch (Exception e) {
				e.printStackTrace(); // show full error in console
			}
		};
	}

}

