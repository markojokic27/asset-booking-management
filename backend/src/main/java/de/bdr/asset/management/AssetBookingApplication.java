package de.bdr.asset.management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AssetBookingApplication {

	public static void main(String[] args) {

		SpringApplication.run(AssetBookingApplication.class, args);
	}

}
