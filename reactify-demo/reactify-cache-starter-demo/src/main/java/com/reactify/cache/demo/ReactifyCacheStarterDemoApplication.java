package com.reactify.cache.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
		"com.reactify.cache.demo",
		"io.hoangtien2k3.cache"
})
public class ReactifyCacheStarterDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReactifyCacheStarterDemoApplication.class, args);
	}

}
