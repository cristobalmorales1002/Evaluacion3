package com.example.demo.dto;

import lombok.Data;
import java.util.List;

/**
 * DTO que representa la solicitud para crear una nueva cotización.
 * Contiene la lista de ítems que el cliente desea cotizar.
 */
@Data
public class CotizacionRequestDTO {

    // Solo necesitamos la lista de items
    private List<ItemCotizacionRequestDTO> items;
}