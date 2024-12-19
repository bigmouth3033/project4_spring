package com.service.main;

import com.service.main.service.azure.AzureReceiver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling
public class MainApplication implements CommandLineRunner {

	@Autowired
	private AzureReceiver _azureReceiver;

	public static void main(String[] args) {
		SpringApplication.run(MainApplication.class, args);
	}

	@Override
	public void run(String... args) {
		try {
			_azureReceiver.ReceiveMessages();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt(); // Đặt lại trạng thái ngắt
			System.err.println("Message receiving interrupted: " + e.getMessage());
		}
	}
}
