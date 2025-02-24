package org.agomez.msvc_users;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
public class MsvcUsersApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsvcUsersApplication.class, args);
	}

}
