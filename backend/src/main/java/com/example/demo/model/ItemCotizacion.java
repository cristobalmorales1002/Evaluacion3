package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "items_cotizacion")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemCotizacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idItem;

    @Column(nullable = false)
    private Integer cantidad;

    @ManyToOne
    @JoinColumn(name = "id_cotizacion", nullable = false)
    @JsonIgnore
    @ToString.Exclude // Evita bucles infinitos en los logs
    @EqualsAndHashCode.Exclude
    private Cotizacion cotizacion;

    @ManyToOne
    @JoinColumn(name = "id_mueble", nullable = false)
    private Mueble mueble;

    // 🔥 LA SOLUCIÓN: fetch = FetchType.EAGER 🔥
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "item_variantes",
            joinColumns = @JoinColumn(name = "id_item"),
            inverseJoinColumns = @JoinColumn(name = "id_variante")
    )
    @ToString.Exclude // Importante para prevenir errores de memoria
    @EqualsAndHashCode.Exclude
    private Set<Variante> variantes;

    @Column(name = "precio_item_final")
    private Double precioItemFinal;
}