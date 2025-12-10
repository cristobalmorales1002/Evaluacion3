// Archivo: src/test/java/com/example/demo/service/CotizacionServiceTest.java
package com.example.demo;

import com.example.demo.dto.CotizacionRequestDTO; // DTO de Petición
import com.example.demo.dto.ItemCotizacionRequestDTO; // DTO de Petición
import com.example.demo.model.*; // Importa todas las Entidades
import com.example.demo.repository.CotizacionRepository;
import com.example.demo.repository.MuebleRepository;
import com.example.demo.repository.VarianteRepository;
import com.example.demo.service.CotizacionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para CotizacionService.
 * Probamos la lógica de negocio clave: Precios y Stock/Ventas.
 * (Esta versión asume que el servicio DEVUELVE la Entidad Cotizacion)
 */
@ExtendWith(MockitoExtension.class)
class CotizacionServiceTest {

    // --- Mocks de Repositorios ---
    @Mock private CotizacionRepository cotizacionRepository;
    @Mock private MuebleRepository muebleRepository;
    @Mock private VarianteRepository varianteRepository;

    // --- Servicio a Probar ---
    @InjectMocks private CotizacionServiceImpl cotizacionService;

    // --- Datos de Prueba ---
    private Mueble mueblePrueba;
    private Variante variantePrueba;

    @BeforeEach
    void setUp() {
        // Mueble: 50.000, Stock 10
        mueblePrueba = Mueble.builder()
                .idMueble(1L).nombreMueble("Silla").precioBase(50000.0).stock(10)
                .build();

        // Variante: 15.000
        variantePrueba = Variante.builder()
                .idVariante(1L).nombre("Barniz").precioAdicional(15000.0)
                .build();
    }

    // =========================================================
    // TEST 1: SERVICIO DE PRECIOS (Variantes)
    // =========================================================
    @Test
    void testCrearCotizacion_CalculoDePrecioCorrecto() {
        // Configuración (Arrange)
        // 1. La Petición (DTO): 1 Silla (ID 1), 1 Variante (ID 1)
        CotizacionRequestDTO requestDTO = new CotizacionRequestDTO();
        ItemCotizacionRequestDTO itemDTO = new ItemCotizacionRequestDTO();
        itemDTO.setIdMueble(1L);
        itemDTO.setCantidad(1);
        itemDTO.setIdVariantes(Set.of(1L));
        requestDTO.setItems(List.of(itemDTO));

        // 2. Mockear lo que busca en la BD
        when(muebleRepository.findById(1L)).thenReturn(Optional.of(mueblePrueba));
        when(varianteRepository.findById(1L)).thenReturn(Optional.of(variantePrueba));

        // 3. Mockear el guardado (devuelve la entidad que le pasan)
        when(cotizacionRepository.save(any(Cotizacion.class))).thenAnswer(invocation -> {
            Cotizacion c = invocation.getArgument(0);
            c.setIdCotizacion(1L); // Simulamos que la BD le dio un ID
            return c;
        });

        // Acción (Act)
        // ¡CAMBIO! Ahora esperamos la Entidad Cotizacion
        Cotizacion cotizacionGuardada = cotizacionService.crearCotizacionDesdeDTO(requestDTO);

        // Afirmación (Assert)
        assertNotNull(cotizacionGuardada);
        // ¡PRUEBA CLAVE! 50.000 (silla) + 15.000 (barniz) = 65.000
        assertEquals(65000.0, cotizacionGuardada.getTotal());
        assertEquals(1, cotizacionGuardada.getItems().size());
    }

    // =========================================================
    // TEST 2: STOCK / VENTA (Éxito)
    // =========================================================
    @Test
    void testConfirmarVenta_Exito_StockSeDescuenta() {
        // Configuración (Arrange)
        // 1. Creamos una cotización (BD) que pide 2 sillas (Stock inicial es 10)
        ItemCotizacion itemDePrueba = ItemCotizacion.builder()
                .mueble(mueblePrueba).cantidad(2).variantes(Set.of())
                .build();
        Cotizacion cotizacionPendiente = Cotizacion.builder()
                .idCotizacion(5L).estado(EstadoCotizacion.PENDIENTE).items(List.of(itemDePrueba))
                .build();
        itemDePrueba.setCotizacion(cotizacionPendiente);

        // 2. Mockear lo que busca en la BD
        when(cotizacionRepository.findById(5L)).thenReturn(Optional.of(cotizacionPendiente));

        // 3. Mockear el guardado
        when(cotizacionRepository.save(any(Cotizacion.class))).thenAnswer(i -> i.getArgument(0));

        // Acción (Act)
        // ¡CAMBIO! Ahora esperamos la Entidad Cotizacion
        Cotizacion cotizacionConfirmada = cotizacionService.confirmarVenta(5L);

        // Afirmación (Assert)
        assertNotNull(cotizacionConfirmada);
        assertEquals(EstadoCotizacion.CONFIRMADA, cotizacionConfirmada.getEstado());
        // ¡PRUEBA CLAVE! Stock era 10, se vendieron 2. Quedan 8.
        assertEquals(8, mueblePrueba.getStock());
        // Verificamos que se haya guardado el mueble (con el stock actualizado)
        verify(muebleRepository).save(mueblePrueba);
    }

    // =========================================================
    // TEST 3: STOCK / VENTA (Stock Insuficiente)
    // =========================================================
    @Test
    void testConfirmarVenta_Fallo_StockInsuficiente() {
        // Configuración (Arrange)
        // 1. Creamos una cotización (BD) que pide 20 sillas (Stock inicial es 10)
        ItemCotizacion itemDePrueba = ItemCotizacion.builder()
                .mueble(mueblePrueba).cantidad(20).variantes(Set.of())
                .build();
        Cotizacion cotizacionPendiente = Cotizacion.builder()
                .idCotizacion(6L).estado(EstadoCotizacion.PENDIENTE).items(List.of(itemDePrueba))
                .build();
        itemDePrueba.setCotizacion(cotizacionPendiente);

        // 2. Mockear lo que busca en la BD
        when(cotizacionRepository.findById(6L)).thenReturn(Optional.of(cotizacionPendiente));

        // Acción (Act) y Afirmación (Assert)
        // ¡PRUEBA CLAVE! Verificamos que se lance la excepción correcta
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cotizacionService.confirmarVenta(6L);
        });

        // Verificamos el mensaje de error (Requisito del PDF)
        assertTrue(exception.getMessage().startsWith("Stock insuficiente"));

        // Verificamos que el stock NO cambió
        assertEquals(10, mueblePrueba.getStock());
        // Verificamos que NUNCA se intentó guardar el mueble
        verify(muebleRepository, never()).save(any(Mueble.class));
    }
}