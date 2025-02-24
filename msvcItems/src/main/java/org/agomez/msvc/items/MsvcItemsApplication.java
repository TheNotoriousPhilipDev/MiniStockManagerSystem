package org.agomez.msvc.items;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})//This is necessary to avoid the error of not having a datasource
public class MsvcItemsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsvcItemsApplication.class, args);
	}

}
