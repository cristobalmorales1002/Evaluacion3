package com.example.demo.repository;

import com.example.demo.model.Mueble;
import com.example.demo.model.EstadoMueble;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MuebleRepository extends JpaRepository<Mueble, Long> {

    // Spring Data JPA creará la consulta automáticamente
    // basado en el nombre del método.

    // Método para buscar muebles por su estado (ACTIVO / INACTIVO)
    List<Mueble> findByEstado(EstadoMueble estado);

    // Método para buscar muebles por tipo (ej. "silla", "mesa")
    List<Mueble> findByTipo(String tipo);
}