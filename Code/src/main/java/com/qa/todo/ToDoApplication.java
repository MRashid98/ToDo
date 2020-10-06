package com.qa.todo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
public class ToDoApplication {

	public static void main(String[] args) {
		ApplicationContext beanBag = SpringApplication.run(ToDoApplication.class, args);
	}

}
