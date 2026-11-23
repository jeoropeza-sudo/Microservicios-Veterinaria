package cl.veterinaria.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApiGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
        System.out.println("================================================");
        System.out.println(" API Gateway iniciado: http://localhost:8090");
        System.out.println(" Eureka: http://localhost:8761");
        System.out.println("================================================");
    }
}
