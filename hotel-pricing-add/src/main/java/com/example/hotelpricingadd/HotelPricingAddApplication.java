package com.example.hotelpricingadd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class HotelPricingAddApplication {

	public static void main(String[] args) {
		SpringApplication.run(HotelPricingAddApplication.class, args);
	}

}
