package org.speech4j.tenantservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.reactive.error.ErrorWebFluxAutoConfiguration;

@SpringBootApplication(exclude = ErrorWebFluxAutoConfiguration.class)
public class TenantServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TenantServiceApplication.class);


	}

//	@Bean
//	CommandLineRunner init(TenantRepository repository){
//		return args -> {
//			Flux<Tenant> coffeeFlux = Flux.just(
//					new Tenant("1", "Tenant-1"),
//					new Tenant("2", "Tenant-2")
//			).flatMap(repository::save);
//
//			coffeeFlux.thenMany(repository.findAll())
//					.subscribe(System.out::println);
//		};
//	}



}