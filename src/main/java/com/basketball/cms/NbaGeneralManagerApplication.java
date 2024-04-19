package com.basketball.cms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class NbaGeneralManagerApplication {

	public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(NbaGeneralManagerApplication.class, args);
                 if (context.containsBean("dataSource")) {
            System.out.println("Successfully connected to MySQL database!");
        } else {
            System.out.println("Failed to connect to MySQL database!");
        }


	} 

}
