package com.veterinaria.ms_productos.config;

import com.veterinaria.ms_productos.model.Producto;
import com.veterinaria.ms_productos.repository.ProductoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initProductos(ProductoRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                List<Producto> iniciales = List.of(
                    new Producto("ME001", "Amoxibay 250mg", "Antibióticos", "Amoxicilina", "Blíster 10 comp.", "Perro / Gato", 45, 4200, "/imagenes/amoxibay.webp", "Presentación: blíster de 10 comprimidos."),
                    new Producto("ME002", "Enrox 50mg", "Antibióticos", "Enrofloxacino", "Blíster 10 comp.", "Perro / Gato", 30, 6800, "/imagenes/en.jpg", "Presentación: blíster de 10 comprimidos."),
                    new Producto("ME004", "Nexgard", "Antiparasitarios", "Afoxolaner", "Masticable 1 unid.", "Perro", 60, 9500, "/imagenes/nex.webp", "Presentación: comprimido masticable unitario."),
                    new Producto("ME005", "Bravecto", "Antiparasitarios", "Fluralaner", "Masticable 1 unid.", "Perro", 40, 18900, "/imagenes/bra.jpg", "Presentación: comprimido masticable unitario."),
                    new Producto("ME006", "Revolution Plus", "Antiparasitarios", "Selamectina + Sarolaner", "Pipeta 1 unid.", "Gato", 35, 14500, "/imagenes/rev.jpg", "Presentación: pipeta unitaria."),
                    new Producto("ME009", "Meloxicam 1mg", "Antiinflamatorios", "Meloxicam", "Blíster 10 comp.", "Perro / Gato", 55, 4500, "/imagenes/melo.jpg", "Presentación: blíster de 10 comprimidos."),
                    new Producto("ME013", "Apoquel 16mg", "Dermatología", "Oclacitinib", "Blíster 10 comp.", "Perro", 18, 22000, "/imagenes/apoquel.webp", "Presentación: blíster de 10 comprimidos."),
                    new Producto("ME018", "Nobivac DHPPi", "Vacunas", "Vacuna polivalente", "Vial 1 dosis", "Perro", 48, 8500, "/imagenes/dhpp.jpeg", "Presentación: vial de una dosis."),
                    new Producto("ME022", "Condrovet forte", "Suplementos", "Condroitín + Glucosamina", "Blíster 30 comp.", "Perro", 25, 14500, "/imagenes/condro.png", "Presentación: blíster de 30 comprimidos.")
                );
                repository.saveAll(iniciales);
                System.out.println(">>> Productos iniciales cargados en productos_db <<<");
            }
        };
    }
}