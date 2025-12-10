// Archivo: src/test/java/com/example/demo/service/MuebleServiceTest.java
package com.example.demo;

import com.example.demo.model.EstadoMueble;
import com.example.demo.model.Mueble;
import com.example.demo.model.TamañoMueble;
import com.example.demo.repository.MuebleRepository;
import com.example.demo.service.MuebleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

// Importaciones estáticas para limpiar el código
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para MuebleService.
 * Probamos la lógica del CRUD de catálogo.
 */
@ExtendWith(MockitoExtension.class) // Activa Mockito
class MuebleServiceTest {

    @Mock
    private MuebleRepository muebleRepository;

    @InjectMocks
    private MuebleServiceImpl muebleService;

    private Mueble mueblePrueba;

    @BeforeEach
    void setUp() {
        // Creamos un mueble de prueba antes de cada test
        mueblePrueba = Mueble.builder()
                .idMueble(1L)
                .nombreMueble("Silla de Prueba")
                .tipo("Silla")
                .precioBase(50000.0)
                .stock(20)
                .estado(EstadoMueble.ACTIVO)
                .tamaño(TamañoMueble.MEDIANO)
                .material("Test")
                .build();
    }

    @Test
    void testGuardarMueble() {
        // Configuración
        when(muebleRepository.save(any(Mueble.class))).thenReturn(mueblePrueba);

        // Acción
        Mueble muebleGuardado = muebleService.guardarMueble(mueblePrueba);

        // Afirmación
        assertNotNull(muebleGuardado);
        assertEquals("Silla de Prueba", muebleGuardado.getNombreMueble());
        verify(muebleRepository).save(mueblePrueba);
    }

    @Test
    void testDesactivarMueble() {
        // Configuración
        when(muebleRepository.findById(1L)).thenReturn(Optional.of(mueblePrueba));
        when(muebleRepository.save(any(Mueble.class))).thenReturn(mueblePrueba);

        // Acción
        Mueble muebleDesactivado = muebleService.desactivarMueble(1L);

        // Afirmación
        assertNotNull(muebleDesactivado);
        assertEquals(EstadoMueble.INACTIVO, muebleDesactivado.getEstado());
    }

    @Test
    void testObtenerMueble_NoEncontrado() {
        // Configuración
        when(muebleRepository.findById(99L)).thenReturn(Optional.empty());

        // Acción
        Optional<Mueble> resultado = muebleService.obtenerMueblePorId(99L);

        // Afirmación
        assertTrue(resultado.isEmpty());
    }
}