package com.crud.crud_lombok_dto.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("PML Project - API")
                        .version("1.0")
                        .description("API documentation for PML Project")
                        .contact(new Contact().email("gaurav@gmail.com").name("Kumar").url("abc@gmail.com")));
    }

}
