package com.file.uploader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UploaderApplication {

	public static void main(String[] args) {
		System.out.println(Runtime.getRuntime().maxMemory());
		SpringApplication.run(UploaderApplication.class, args);
	}

}
