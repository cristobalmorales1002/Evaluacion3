package com.example.demo.service;

import com.example.demo.dto.CotizacionRequestDTO;
import com.example.demo.dto.ItemCotizacionRequestDTO;
import com.example.demo.model.*;
import com.example.demo.repository.CotizacionRepository;
import com.example.demo.repository.MuebleRepository;
import com.example.demo.repository.VarianteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CotizacionServiceImpl implements CotizacionService {

    @Autowired
    private CotizacionRepository cotizacionRepository;
    @Autowired
    private MuebleRepository muebleRepository;
    @Autowired
    private VarianteRepository varianteRepository;

    @Override
    @Transactional
    public Cotizacion crearCotizacionDesdeDTO(CotizacionRequestDTO dto) {
        Cotizacion cotizacion = new Cotizacion();
        cotizacion.setFecha(LocalDate.now());
        cotizacion.setEstado(EstadoCotizacion.PENDIENTE);

        double totalCotizacion = 0.0;
        List<ItemCotizacion> items = new ArrayList<>();

        for (ItemCotizacionRequestDTO itemDTO : dto.getItems()) {
            Mueble mueble = muebleRepository.findById(itemDTO.getIdMueble())
                    .orElseThrow(() -> new RuntimeException("Mueble no encontrado: " + itemDTO.getIdMueble()));

            Set<Variante> variantes = new HashSet<>();
            if (itemDTO.getIdVariantes() != null && !itemDTO.getIdVariantes().isEmpty()) {
                variantes = itemDTO.getIdVariantes().stream()
                        .map(id -> varianteRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Variante no encontrada: " + id)))
                        .collect(Collectors.toSet());
            }

            double precioItem = mueble.getPrecioBase();
            for (Variante v : variantes) {
                precioItem += v.getPrecioAdicional();
            }
            double precioTotalItem = precioItem * itemDTO.getCantidad();
            totalCotizacion += precioTotalItem;

            ItemCotizacion item = ItemCotizacion.builder()
                    .cantidad(itemDTO.getCantidad())
                    .mueble(mueble)
                    .variantes(variantes)
                    .precioItemFinal(precioTotalItem)
                    .cotizacion(cotizacion)
                    .build();

            items.add(item);
        }

        cotizacion.setItems(items);
        cotizacion.setTotal(totalCotizacion);
        return cotizacionRepository.save(cotizacion);
    }

    @Override
    @Transactional
    public Cotizacion confirmarVenta(Long idCotizacion) {
        Cotizacion cotizacion = cotizacionRepository.findById(idCotizacion)
                .orElseThrow(() -> new RuntimeException("Cotización no encontrada con id: " + idCotizacion));

        if (cotizacion.getEstado() == EstadoCotizacion.CONFIRMADA) {
            throw new RuntimeException("Esta cotización ya fue confirmada");
        }

        // --- VERIFICACIÓN DE STOCK ---
        // Al usar EAGER en la entidad, las variantes ya vienen cargadas.
        // NO AGREGAR .size() AQUÍ.
        for (ItemCotizacion item : cotizacion.getItems()) {
            Mueble mueble = item.getMueble();
            if (mueble.getStock() < item.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para el producto: " + mueble.getNombreMueble());
            }
        }

        // --- DESCUENTO DE STOCK ---
        for (ItemCotizacion item : cotizacion.getItems()) {
            Mueble mueble = item.getMueble();
            int nuevoStock = mueble.getStock() - item.getCantidad();
            mueble.setStock(nuevoStock);
            muebleRepository.save(mueble);
        }

        cotizacion.setEstado(EstadoCotizacion.CONFIRMADA);
        return cotizacionRepository.save(cotizacion);
    }

    @Override
    public Optional<Cotizacion> obtenerCotizacionPorId(Long id) {
        return cotizacionRepository.findById(id);
    }

    @Override
    public List<Cotizacion> listarTodasLasCotizaciones() {
        return cotizacionRepository.findAll();
    }
}