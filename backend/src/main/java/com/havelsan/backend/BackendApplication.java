package com.havelsan.backend;

import com.havelsan.backend.entity.UserDTO;
import com.havelsan.backend.model.User;
import com.havelsan.backend.repository.UserRepository;
import com.havelsan.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class BackendApplication implements CommandLineRunner {

	private final UserService userService;

	public static void main(String[] args) {
		//is starting the IoC container
		SpringApplication.run(BackendApplication.class, args);
	}

	@Override
	public void run(String... args) {
		UserDTO user = new UserDTO();
		user.setUsername("admin");
		user.setPassword("1234");
		this.userService.register(user);
	}
}
