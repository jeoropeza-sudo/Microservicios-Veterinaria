package com.tienda.carrito;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;

import java.util.List;

@SpringBootApplication
public class CarritoApplication {

    public static void main(String[] args) {
        SpringApplication.run(CarritoApplication.class, args);
    }

    @Bean
    public OpenAPI openAPI() {
        Server localServer = new Server()
                .url("http://localhost:8085")
                .description("Microservicio de carrito");

        return new OpenAPI().servers(List.of(localServer));
    }
}
