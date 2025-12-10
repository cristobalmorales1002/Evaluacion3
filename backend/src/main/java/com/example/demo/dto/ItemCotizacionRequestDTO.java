package com.example.demo.dto;

import lombok.Data;
import java.util.Set;

/**
 * DTO que representa un ítem individual dentro de una solicitud de cotización.
 * Especifica qué mueble, qué cantidad y qué variantes se desean.
 */
@Data
public class ItemCotizacionRequestDTO {

    // El ID del mueble que quieren comprar
    private Long idMueble;

    // Cuántas unidades de ese mueble
    private Integer cantidad;

    // Un Set (lista sin duplicados) de los IDs de las variantes
    // Ej: [1, 3] (ID 1 = "Barniz Premium", ID 3 = "Ruedas")
    private Set<Long> idVariantes;
}