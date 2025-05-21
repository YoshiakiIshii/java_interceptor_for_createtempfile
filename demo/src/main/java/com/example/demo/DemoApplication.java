package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;

@SpringBootApplication
public class DemoApplication {
	public DemoApplication() {
		try {
			Path tempFile = Files.createTempFile(Paths.get("/tmp"), "tempFile_", ".txt");
			String timestamp = "Timestamp: " + LocalDateTime.now().toString();
			Files.write(tempFile, timestamp.getBytes(), StandardOpenOption.WRITE);
			System.out.println("Temporary file created at: " + tempFile.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml")) {
			SpringApplication.run(DemoApplication.class, args);
		}
	}

}
