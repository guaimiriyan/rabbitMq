package com.angus.esjob;

import com.angus.rabbitmq.task.annotation.EnableElasticJob;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@EnableElasticJob
@SpringBootApplication
@ComponentScan(basePackages = {"com.angus.esjob.*","com.angus.esjob.service.*", "com.angus.esjob.annotation.*","com.angus.esjob.task.*"})
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
