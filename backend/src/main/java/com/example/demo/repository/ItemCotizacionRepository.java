package com.example.demo.repository;

import com.example.demo.model.ItemCotizacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemCotizacionRepository extends JpaRepository<ItemCotizacion, Long> {

    // Aquí podríamos buscar ítems por mueble, por ejemplo
    // List<ItemCotizacion> findByMueble(Mueble mueble);
}