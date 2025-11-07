package com.desafio_pleno.erp_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
		info = @Info(
				title = "ERP System API",
				version = "v1",
				description = "API de Clientes, Produtos e Pedidos"
		)
)
@SpringBootApplication
public class FreireApplication {

	public static void main(String[] args) {
		SpringApplication.run(FreireApplication.class, args);
	}

}
