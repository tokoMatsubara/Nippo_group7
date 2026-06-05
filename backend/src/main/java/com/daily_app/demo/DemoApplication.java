package com.daily_app.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync // ←非同期処理を有効化するために必要らしいです
@SpringBootApplication
@EnableScheduling // ←Spring Bootで @Scheduled を有効にするのに必要らしい
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
