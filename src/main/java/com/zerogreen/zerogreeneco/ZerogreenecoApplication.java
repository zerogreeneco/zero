package com.zerogreen.zerogreeneco;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ZerogreenecoApplication {
//	public static void main(String[] args) {
//		SpringApplication application = new SpringApplication(ZerogreenecoApplication.class);
//		application.addListeners(new ApplicationPidFileWriter());
//		application.run(args);
//
//	}
	public static void main(String[] args) {
		SpringApplication.run(ZerogreenecoApplication.class, args);
	}

}
