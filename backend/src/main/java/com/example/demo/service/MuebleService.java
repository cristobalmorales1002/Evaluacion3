package com.example.demo.service;

import com.example.demo.model.Mueble;
import java.util.List;
import java.util.Optional;

public interface MuebleService {

    // CREATE
    Mueble guardarMueble(Mueble mueble);

    // READ
    List<Mueble> listarTodosLosMuebles();
    List<Mueble> listarMueblesActivos();
    Optional<Mueble> obtenerMueblePorId(Long id);

    // UPDATE
    Mueble actualizarMueble(Long id, Mueble muebleActualizado);
    Mueble desactivarMueble(Long id);
    Mueble activarMueble(Long id);

    // DELETE (Aunque la evaluación pide "desactivar",
    // dejamos el borrar por si acaso, aunque es mejor no usarlo)
    // void eliminarMueble(Long id);
}