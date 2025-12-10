package com.example.demo.service;

import com.example.demo.model.Mueble;
import com.example.demo.model.EstadoMueble;
import com.example.demo.repository.MuebleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MuebleServiceImpl implements MuebleService {

    @Autowired
    private MuebleRepository muebleRepository;

    // CREATE
    @Override
    public Mueble guardarMueble(Mueble mueble) {
        // Asegurarnos de que los muebles nuevos se creen como ACTIVOS
        // a menos que se especifique lo contrario
        if (mueble.getEstado() == null) {
            mueble.setEstado(EstadoMueble.ACTIVO);
        }
        return muebleRepository.save(mueble);
    }

    // READ
    @Override
    public List<Mueble> listarTodosLosMuebles() {
        return muebleRepository.findAll();
    }

    @Override
    public List<Mueble> listarMueblesActivos() {
        return muebleRepository.findByEstado(EstadoMueble.ACTIVO);
    }

    @Override
    public Optional<Mueble> obtenerMueblePorId(Long id) {
        return muebleRepository.findById(id);
    }

    // UPDATE
    @Override
    public Mueble actualizarMueble(Long id, Mueble muebleActualizado) {
        // Buscamos el mueble existente
        return muebleRepository.findById(id).map(muebleExistente -> {
            // Actualizamos solo los campos que nos interesan
            muebleExistente.setNombreMueble(muebleActualizado.getNombreMueble());
            muebleExistente.setTipo(muebleActualizado.getTipo());
            muebleExistente.setPrecioBase(muebleActualizado.getPrecioBase());
            muebleExistente.setStock(muebleActualizado.getStock());
            muebleExistente.setEstado(muebleActualizado.getEstado());
            muebleExistente.setTamaño(muebleActualizado.getTamaño());
            muebleExistente.setMaterial(muebleActualizado.getMaterial());
            return muebleRepository.save(muebleExistente);
        }).orElseThrow(() -> new RuntimeException("Mueble no encontrado con id: " + id));
        // (En un proyecto real, crearíamos excepciones personalizadas)
    }

    // "DELETE" Lógico (Desactivar)
    @Override
    public Mueble desactivarMueble(Long id) {
        return muebleRepository.findById(id).map(mueble -> {
            mueble.setEstado(EstadoMueble.INACTIVO);
            return muebleRepository.save(mueble);
        }).orElseThrow(() -> new RuntimeException("Mueble no encontrado con id: " + id));
    }

    @Override
    public Mueble activarMueble(Long id) {
        return muebleRepository.findById(id).map(mueble -> {
            mueble.setEstado(EstadoMueble.ACTIVO);
            return muebleRepository.save(mueble);
        }).orElseThrow(() -> new RuntimeException("Mueble no encontrado con id: " + id));
    }
}