package org.xpd.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.web.ErrorMvcAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

/**
 * @author pawan juneja
 *
 */
@SpringBootApplication(scanBasePackages = { "org.xpd.supplier.controller", "org.xpd.buyer.controller",
		"org.xpd.app.controller" })
@EnableJpaRepositories({ "org.xpd.repo" })

@EntityScan(basePackages = { "org.xpd.models" })
@ComponentScan(basePackages = { "org.xpd.supplier.service", "org.xpd.app.service", "org.xpd.supplier.controller",
		"org.xpd.buyer.controller", "org.xpd.buyer.service", "org.xpd.app.controller", "org.xpd.app.schedule",
		"org.xpd.app," + "org.xpd.util" })
@EnableScheduling
@Configuration
public class App extends SpringBootServletInitializer {
	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(App.class);
	}

	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}
}
