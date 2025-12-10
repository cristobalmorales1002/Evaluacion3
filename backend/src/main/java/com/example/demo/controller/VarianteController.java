package com.example.demo.controller;

import com.example.demo.model.Variante;
import com.example.demo.service.VarianteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/variantes") // Esta es la "puerta" que la App está buscando
public class VarianteController {

    @Autowired
    private VarianteService varianteService;

    // 1. Endpoint para CREAR variantes (Esto es lo que usa la prueba)
    @PostMapping
    public ResponseEntity<Variante> crearVariante(@RequestBody Variante variante) {
        Variante nuevaVariante = varianteService.crearVariante(variante);
        return new ResponseEntity<>(nuevaVariante, HttpStatus.CREATED);
    }

    // 2. Endpoint para LISTAR (Útil para verificar)
    @GetMapping
    public ResponseEntity<List<Variante>> listarVariantes() {
        return ResponseEntity.ok(varianteService.obtenerTodasLasVariantes());
    }

    // 3. Endpoint para ELIMINAR (Para limpieza)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarVariante(@PathVariable Long id) {
        varianteService.eliminarVariante(id);
        return ResponseEntity.noContent().build();
    }
}