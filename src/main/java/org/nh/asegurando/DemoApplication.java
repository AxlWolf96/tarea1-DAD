package org.nh.asegurando;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	public CommandLineRunner setup(UserRepository br) {
		return (args) -> {
			br.save(new User("Jorge Guerra", 50, 2000.00));
			br.save(new User("Carlos Ruiz", 45, 1800.00));
			br.save(new User("Tomas Tafur", 36, 2000.00));
			br.save(new User("Maria Velasquez", 29, 3600.00));
			br.save(new User("German Suarez", 65, 2600.00));
			br.save(new User("Luis Avendaño", 38, 3100.00));
			br.save(new User("Rossina Gonzales", 42, 4300.00));
			br.save(new User("Martha Zegarra", 51, 2200.00));
		};
	}
}
