package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cotizaciones")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cotizacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCotizacion;

    @Column(nullable = false)
    private LocalDate fecha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoCotizacion estado; // (PENDIENTE, CONFIRMADA)

    // Relación: Una cotización tiene muchos items (muebles)
    // CascadeType.ALL: Si guardo/borro una cotización, guarda/borra sus items
    // orphanRemoval=true: Si quito un item de la lista, se borra de la BD
    @OneToMany(mappedBy = "cotizacion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemCotizacion> items;

    // Podrías agregar un campo total, aunque es mejor calcularlo
    private Double total;

}