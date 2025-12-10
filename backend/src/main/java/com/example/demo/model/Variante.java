package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "variantes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Variante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVariante;

    @Column(nullable = false, unique = true)
    private String nombre; // Ej: "Barniz Premium", "Cojines de Seda"

    @Column(name = "precio_adicional", nullable = false)
    private Double precioAdicional; //

    // Relación: Una variante puede estar en muchos items de cotización
    @ManyToMany(mappedBy = "variantes")
    @JsonIgnore
    private Set<ItemCotizacion> itemsCotizacion;
}