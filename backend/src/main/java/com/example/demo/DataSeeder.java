package com.example.demo;

// Asegúrate de que estos 'import' coincidan con tus paquetes de 'model' y 'repository'
import com.example.demo.model.EstadoMueble;
import com.example.demo.model.Mueble;
import com.example.demo.model.TamañoMueble;
import com.example.demo.model.Variante;
import com.example.demo.repository.MuebleRepository;
import com.example.demo.repository.VarianteRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Esta clase es un 'semillador' que se ejecuta una sola vez
 * al iniciar la aplicación de Spring Boot.
 * Su propósito es poblar la base de datos con datos de prueba.
 */
@Component
public class DataSeeder implements CommandLineRunner {

    // Inyectamos los repositorios que necesitamos para guardar datos
    @Autowired
    private MuebleRepository muebleRepository;

    @Autowired
    private VarianteRepository varianteRepository;

    /**
     * Este es el método principal que Spring Boot ejecutará al arrancar.
     */
    @Override
    public void run(String... args) throws Exception {
        System.out.println("--- [DataSeeder] Iniciando siembra de datos... ---");

        // Llamamos a los métodos helper para cargar los datos
        cargarMueblesSiEsNecesario();
        cargarVariantesSiEsNecesario();

        System.out.println("--- [DataSeeder] Siembra de datos finalizada. ---");
    }

    /**
     * Revisa si la tabla de muebles está vacía.
     * Si lo está, la puebla con datos de prueba.
     */
    private void cargarMueblesSiEsNecesario() {
        // .count() nos dice cuántos registros hay
        if (muebleRepository.count() == 0) {
            System.out.println("Tabla 'muebles' está vacía. Cargando datos de prueba...");

            Mueble silla = Mueble.builder()
                    .nombreMueble("Silla de Comedor Clásica")
                    .tipo("Silla")
                    .precioBase(50000.0)
                    .stock(20)
                    .estado(EstadoMueble.ACTIVO)
                    .tamaño(TamañoMueble.MEDIANO)
                    .material("Madera de Roble")
                    .build();

            Mueble mesa = Mueble.builder()
                    .nombreMueble("Mesa de Centro Moderna")
                    .tipo("Mesa")
                    .precioBase(120000.0)
                    .stock(10)
                    .estado(EstadoMueble.ACTIVO)
                    .tamaño(TamañoMueble.GRANDE)
                    .material("Vidrio y Metal")
                    .build();

            Mueble sillon = Mueble.builder()
                    .nombreMueble("Sillón de Lectura")
                    .tipo("Sillon")
                    .precioBase(85000.0)
                    .stock(5)
                    .estado(EstadoMueble.ACTIVO)
                    .tamaño(TamañoMueble.MEDIANO)
                    .material("Tela")
                    .build();

            // Guardamos los muebles en la base de datos
            muebleRepository.save(silla);
            muebleRepository.save(mesa);
            muebleRepository.save(sillon);

            System.out.println("-> 3 muebles guardados.");
        } else {
            System.out.println("Tabla 'muebles' ya tiene datos. No se cargará nada.");
        }
    }

    /**
     * Revisa si la tabla de variantes está vacía.
     * Si lo está, la puebla con datos de prueba.
     */
    private void cargarVariantesSiEsNecesario() {
        if (varianteRepository.count() == 0) {
            System.out.println("Tabla 'variantes' está vacía. Cargando datos de prueba...");

            Variante barniz = Variante.builder()
                    .nombre("Barniz Premium Anti-rayas")
                    .precioAdicional(15000.0)
                    .build();

            Variante cojines = Variante.builder()
                    .nombre("Cojines de Seda")
                    .precioAdicional(25000.0)
                    .build();

            Variante ruedas = Variante.builder()
                    .nombre("Ruedas de Goma")
                    .precioAdicional(10000.0)
                    .build();

            // Guardamos las variantes en la base de datos
            varianteRepository.save(barniz);
            varianteRepository.save(cojines);
            varianteRepository.save(ruedas);

            System.out.println("-> 3 variantes guardadas.");
        } else {
            System.out.println("Tabla 'variantes' ya tiene datos. No se cargará nada.");
        }
    }
}