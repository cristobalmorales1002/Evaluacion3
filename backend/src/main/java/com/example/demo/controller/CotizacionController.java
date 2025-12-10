package com.example.demo.controller;

import com.example.demo.model.Cotizacion;
import com.example.demo.service.CotizacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/cotizaciones")
public class CotizacionController {

    @Autowired
    private CotizacionService cotizacionService;

    // Crear Cotización
    @PostMapping
    public ResponseEntity<Cotizacion> crearCotizacion(@RequestBody com.example.demo.dto.CotizacionRequestDTO dto) {
        try {
            Cotizacion nuevaCotizacion = cotizacionService.crearCotizacionDesdeDTO(dto);
            return new ResponseEntity<>(nuevaCotizacion, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            e.printStackTrace(); // Ver error en consola
            return ResponseEntity.badRequest().build();
        }
    }

    // Confirmar Venta (AQUÍ ESTABA EL ERROR DEL CONTROLADOR)
    @PostMapping("/{id}/confirmar")
    public ResponseEntity<?> confirmarVenta(@PathVariable Long id) {
        try {
            Cotizacion ventaConfirmada = cotizacionService.confirmarVenta(id);

            // Devolvemos un JSON simple para evitar errores de recursión
            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("mensaje", "Venta confirmada con éxito");
            respuesta.put("idCotizacion", ventaConfirmada.getIdCotizacion());
            respuesta.put("estado", ventaConfirmada.getEstado());
            respuesta.put("total", ventaConfirmada.getTotal());

            return ResponseEntity.ok(respuesta);

        } catch (RuntimeException e) {
            // Imprimimos el error real en la consola de Docker
            System.out.println("❌ ERROR EN CONFIRMAR VENTA:");
            e.printStackTrace();

            // Manejo seguro del mensaje (evita el NullPointerException anterior)
            String mensajeError = e.getMessage() != null ? e.getMessage() : "Error interno desconocido (Posible NullPointer)";

            if (mensajeError.startsWith("Stock insuficiente")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(mensajeError);
            }
            // Si es otro error (como el NullPointer), devolvemos 500 con el detalle
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(mensajeError);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cotizacion> obtenerCotizacionPorId(@PathVariable Long id) {
        return cotizacionService.obtenerCotizacionPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}