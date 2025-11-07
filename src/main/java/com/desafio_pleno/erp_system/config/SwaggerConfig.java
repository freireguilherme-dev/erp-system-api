package com.desafio_pleno.erp_system.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração opcional do OpenAPI/Swagger para personalizar título, contato,
 * licença e agrupamento de endpoints. O springdoc já funciona sem esta classe.
 */
@Configuration
public class SwaggerConfig {

    /** Documento OpenAPI principal (metadados exibidos na UI). */
    @Bean
    public OpenAPI erpOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("ERP System API")
                        .description("API de Clientes, Produtos e Pedidos")
                        .version("v1")
                        .contact(new Contact()
                                .name("Guilherme Freire")
                                .url("https://www.linkedin.com/in/freireguilherme")
                                .email("freireguilherme2@gmail.com"))
                        .license(new License().name("MIT").url("https://opensource.org/licenses/MIT")))
                .externalDocs(new ExternalDocumentation()
                        .description("README do projeto")
                        .url("https://github.com/<freireguilherme-dev>/erp-system"));
    }

//    /** Grupo padrão: todos os endpoints da API pública. */
//    @Bean
//    public GroupedOpenApi publicApi() {
//        return GroupedOpenApi.builder()
//                .group("public")
//                .pathsToMatch("/api/**")
//                .build();
//    }
}
