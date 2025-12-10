package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "muebles")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Mueble {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mueble")
    private Long idMueble; //

    @Column(name = "nombre_mueble", nullable = false)
    private String nombreMueble; //

    @Column(nullable = false)
    private String tipo; //  (silla, sillon, mesa, etc.)

    @Column(name = "precio_base", nullable = false)
    private Double precioBase; //

    @Column(nullable = false)
    private Integer stock; //

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoMueble estado; //  (ACTIVO, INACTIVO)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TamañoMueble tamaño; //  (GRANDE, MEDIANO, PEQUEÑO)

    @Column(nullable = false)
    private String material; //

    // Relación: Un mueble puede estar en muchos items de cotización
    @OneToMany(mappedBy = "mueble")
    @JsonIgnore
    private List<ItemCotizacion> itemsCotizacion;
}