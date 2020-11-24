package cl.desquite.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class DesquiteBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(DesquiteBackendApplication.class, args);
	}

}
