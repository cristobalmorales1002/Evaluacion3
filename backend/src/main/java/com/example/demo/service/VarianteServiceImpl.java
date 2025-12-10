package com.example.demo.service;

import com.example.demo.model.Variante;
import com.example.demo.repository.VarianteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VarianteServiceImpl implements VarianteService {

    @Autowired
    private VarianteRepository varianteRepository;

    @Override
    public Variante crearVariante(Variante variante) {
        return varianteRepository.save(variante);
    }

    @Override
    public List<Variante> obtenerTodasLasVariantes() {
        return varianteRepository.findAll();
    }

    @Override
    public Optional<Variante> obtenerVariantePorId(Long id) {
        return varianteRepository.findById(id);
    }

    @Override
    public void eliminarVariante(Long id) {
        varianteRepository.deleteById(id);
    }
}