package com.example.demo.repository;

import com.example.demo.model.Cotizacion;
import com.example.demo.model.EstadoCotizacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CotizacionRepository extends JpaRepository<Cotizacion, Long> {

    // Método para buscar cotizaciones por su estado (PENDIENTE / CONFIRMADA)
    List<Cotizacion> findByEstado(EstadoCotizacion estado);
}