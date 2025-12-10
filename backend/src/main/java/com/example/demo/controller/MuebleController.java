package com.example.demo.controller;

import com.example.demo.model.Mueble;
import com.example.demo.service.MuebleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestionar el catálogo de Muebles.
 * Expone los endpoints para el CRUD de muebles.
 */
@RestController
@RequestMapping("/api/muebles") // Ruta base para todos los endpoints de este controlador
public class MuebleController {

    @Autowired
    private MuebleService muebleService;

    /**
     * Endpoint para crear un nuevo mueble.
     * HTTP POST: /api/muebles
     * @param mueble El mueble a crear (viene en el body de la petición).
     * @return El mueble creado con su ID y estado 201 (Created).
     */
    @PostMapping
    public ResponseEntity<Mueble> crearMueble(@RequestBody Mueble mueble) {
        Mueble nuevoMueble = muebleService.guardarMueble(mueble);
        return new ResponseEntity<>(nuevoMueble, HttpStatus.CREATED);
    }

    /**
     * Endpoint para listar todos los muebles ACTIVOS.
     * HTTP GET: /api/muebles
     * @return Lista de muebles activos y estado 200 (OK).
     */
    @GetMapping
    public ResponseEntity<List<Mueble>> listarMueblesActivos() {
        // El requisito es gestionar el catálogo, listamos los activos
        List<Mueble> muebles = muebleService.listarMueblesActivos();
        return ResponseEntity.ok(muebles);
    }

    /**
     * Endpoint para obtener un mueble específico por su ID.
     * HTTP GET: /api/muebles/{id}
     * @param id El ID del mueble a buscar (viene en la URL).
     * @return El mueble encontrado (200 OK) o 404 (Not Found).
     */
    @GetMapping("/{id}")
    public ResponseEntity<Mueble> obtenerMueblePorId(@PathVariable Long id) {
        return muebleService.obtenerMueblePorId(id)
                .map(ResponseEntity::ok) // Si .isPresent() -> 200 OK
                .orElse(ResponseEntity.notFound().build()); // Si no -> 404 Not Found
    }

    /**
     * Endpoint para actualizar un mueble existente.
     * HTTP PUT: /api/muebles/{id}
     * @param id El ID del mueble a actualizar.
     * @param mueble Los datos nuevos del mueble (en el body).
     * @return El mueble actualizado (200 OK) o 404 (Not Found).
     */
    @PutMapping("/{id}")
    public ResponseEntity<Mueble> actualizarMueble(@PathVariable Long id, @RequestBody Mueble mueble) {
        try {
            Mueble muebleActualizado = muebleService.actualizarMueble(id, mueble);
            return ResponseEntity.ok(muebleActualizado);
        } catch (RuntimeException e) {
            // Si el servicio lanza excepción (por no encontrar el ID)
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Endpoint para desactivar un mueble (Borrado Lógico).
     * HTTP DELETE: /api/muebles/{id}
     * @param id El ID del mueble a desactivar.
     * @return El mueble con estado INACTIVO (200 OK) o 404 (Not Found).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Mueble> desactivarMueble(@PathVariable Long id) {
        try {
            Mueble muebleDesactivado = muebleService.desactivarMueble(id);
            return ResponseEntity.ok(muebleDesactivado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}