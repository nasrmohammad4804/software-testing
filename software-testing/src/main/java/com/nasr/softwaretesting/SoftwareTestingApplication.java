package com.nasr.softwaretesting;

import com.nasr.softwaretesting.stripe.StripeService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SoftwareTestingApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(SoftwareTestingApplication.class, args);
	}

}
