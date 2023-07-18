package com.gestion.recettes.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Quantite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Basic
    private String uniteDeMesure;
    @Basic
    private double nombre;
    @ManyToOne(fetch = FetchType.EAGER)
    private Ingredient ingredient;
    @ManyToOne(fetch = FetchType.EAGER)
    private Recette recette;
}