package com.reactify.tracelog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
		"io.hoangtien2k3.tracelog",
		"com.reactify.tracelog"
})
public class ReactifyTracelogStarterDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReactifyTracelogStarterDemoApplication.class, args);
	}

}
