package com.compulynxtest.compulynxtest;

import com.compulynxtest.compulynxtest.role.Role;
import com.compulynxtest.compulynxtest.role.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableAsync
@SpringBootApplication
public class CompulynxtestApplication {

	public static void main(String[] args) {
		SpringApplication.run(CompulynxtestApplication.class, args);
	}
	@Bean
	public CommandLineRunner runner(RoleRepository roleRepository) {
		return args -> {
			if (roleRepository.findByName("CUSTOMER").isEmpty()) {
				roleRepository.save(Role.builder().name("CUSTOMER").build());
			}
		};
	}
}
