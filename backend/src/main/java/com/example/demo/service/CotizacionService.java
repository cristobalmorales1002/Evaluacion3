package com.example.demo.service;

import com.example.demo.dto.CotizacionRequestDTO;
import com.example.demo.model.Cotizacion;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz de servicio para la lógica de negocio de Cotizaciones y Ventas.
 */
public interface CotizacionService {

    /**
     * Crea una nueva cotización (en estado PENDIENTE) a partir de un DTO.
     * @param dto El objeto que contiene los IDs de muebles, variantes y cantidades.
     * @return La cotización creada y guardada en la base de datos.
     */
    Cotizacion crearCotizacionDesdeDTO(CotizacionRequestDTO dto);

    /**
     * Confirma una cotización existente como VENTA.
     * Esto valida el stock y, si es exitoso, descuenta el stock.
     * @param idCotizacion El ID de la cotización a confirmar.
     * @return La cotización actualizada al estado CONFIRMADA.
     * @throws RuntimeException si el stock es insuficiente o la cotización no existe.
     */
    Cotizacion confirmarVenta(Long idCotizacion);

    /**
     * Busca una cotización por su ID.
     * @param id El ID de la cotización.
     * @return Un Optional que contiene la cotización si se encuentra.
     */
    Optional<Cotizacion> obtenerCotizacionPorId(Long id);

    /**
     * Lista todas las cotizaciones en el sistema.
     * @return Una lista de todas las cotizaciones.
     */
    List<Cotizacion> listarTodasLasCotizaciones();
}