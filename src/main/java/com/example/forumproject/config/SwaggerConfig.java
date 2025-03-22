package com.example.forumproject.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info (
                contact = @Contact(
                        name = "Georgi Benchev and Ivan Ivanov",
                        email = "gega4321@gmail.com ; ivanovivanbusiness@gmail.com"
                ),
                description = """
                        This is an **Automobile Forum Management System** developed using the **Spring Boot** framework.
                         The forum allows users to:
                        - **Create posts** related to automobiles.
                        - **Add comments** on posts.
                        - **Add tags** on posts for better filtering.
                        - **Search filtering** on posts based on different attributes.
                        - **React** to posts with likes and dislikes.
                        - **Register and authenticate** using JWT authentication.
                        - **Admins** moderate content to maintain quality.""",
                title = "Automobile Forum Management System API",
                version = "1.0"
        ),
        servers = {
                @Server(
                        description = "Local ENV",
                        url = "http://localhost:8080"
                ),
                @Server(
                        description = "Heroku Deployment",
                        url = "https://moto-forum-23b2887c6d95.herokuapp.com/mvc/home"
                )
        }
)
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication", new io.swagger.v3.oas.models.security.SecurityScheme()
                                .type(io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}