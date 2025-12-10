package com.example.demo.service;

import com.example.demo.model.Variante;
import java.util.List;
import java.util.Optional;

public interface VarianteService {
    Variante crearVariante(Variante variante);
    List<Variante> obtenerTodasLasVariantes();
    Optional<Variante> obtenerVariantePorId(Long id);
    void eliminarVariante(Long id);
}